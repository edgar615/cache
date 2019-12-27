package com.github.edgar615.cache.spring;

import com.google.common.base.Strings;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;

/**
 * Cacheable的增强，增加缓存并发的控制机制，同一个key同时只会有一个向下游发起请求
 */
@Aspect
public class CacheableExtensionAspet {

  private final KeyGenerator keyGenerator = new SimpleKeyGenerator();

  private final ConcurrentMap<String, WeakHashMap<Object, CompletableFuture<Object>>> localLock = new ConcurrentHashMap<>();

  private final BeanFactory beanFactory;

  public CacheableExtensionAspet(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  private synchronized CompletableFuture<Object> putFuture(String cacheName, Object key, CompletableFuture<Object> future) {
    localLock.putIfAbsent(cacheName, new WeakHashMap<>());
    return localLock.get(cacheName).putIfAbsent(key, future);
  }

  private synchronized void removeFuture(String cacheName, Object key, CompletableFuture<Object> future) {
    localLock.get(cacheName).remove(key, future);
  }

  @Around("@annotation(org.springframework.cache.annotation.Cacheable) && @annotation(cacheable)")
  public Object around(ProceedingJoinPoint pjp, Cacheable cacheable) throws Throwable {
    cacheable.keyGenerator();
    KeyGenerator localKeyGenerator = keyGenerator;
    if (!Strings.isNullOrEmpty(cacheable.keyGenerator())) {
      localKeyGenerator = beanFactory.getBean(cacheable.keyGenerator(), KeyGenerator.class);
    }
    // 先只取一个做测试
    String cacheName = cacheable.cacheNames()[0];
    Method method = getSpecificmethod(pjp);
    Object key = localKeyGenerator.generate(pjp.getTarget(), method, pjp.getArgs());

    CompletableFuture<Object> completableFuture = new CompletableFuture<>();
    CompletableFuture<Object> future = putFuture(cacheName, key, completableFuture);
    if (future == null) {
      try {
        System.out.println(future);
        Object result = pjp.proceed();
        completableFuture.complete(result);
        return result;
      } finally {
        removeFuture(cacheName, key, completableFuture);
      }
    } else {
      try {
        return future.get();
      } catch (ExecutionException e) {
        if (e.getCause() instanceof RuntimeException) {
          throw (RuntimeException) e.getCause();
        } else if (e.getCause() instanceof Error) {
          throw (Error) e.getCause();
        }
        throw new CompletionException(e.getCause());
      } catch (InterruptedException e) {
        throw new CompletionException(e);
      }
    }
  }

  private static <T extends Annotation> List<T> getMethodAnnotations(AnnotatedElement ae,
      Class<T> annotationType) {
    List<T> anns = new ArrayList<T>(2);
    // look for raw annotation
    T ann = ae.getAnnotation(annotationType);
    if (ann != null) {
      anns.add(ann);
    }
    // look for meta-annotations
    for (Annotation metaAnn : ae.getAnnotations()) {
      ann = metaAnn.annotationType().getAnnotation(annotationType);
      if (ann != null) {
        anns.add(ann);
      }
    }
    return (anns.isEmpty() ? null : anns);
  }

  private Method getSpecificmethod(ProceedingJoinPoint pjp) {
    MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
    Method method = methodSignature.getMethod();
    // 从目标对象获取到真实的 class 对象，而不是代理 class 类对象
    Class<?> targetClass = AopProxyUtils.ultimateTargetClass(pjp.getTarget());
    if (targetClass == null && pjp.getTarget() != null) {
      targetClass = pjp.getTarget().getClass();
    }
    Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
    // If we are dealing with method with generic parameters, find the
    // original method.
    specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
    return specificMethod;
  }

}
