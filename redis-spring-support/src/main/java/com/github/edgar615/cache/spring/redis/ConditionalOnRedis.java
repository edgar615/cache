package com.github.edgar615.cache.spring.redis;

import com.github.edgar615.cache.spring.redis.ConditionalOnRedis.RedisCondition;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(RedisCondition.class)
public @interface ConditionalOnRedis {

  public static class RedisCondition implements Condition {

    private static final String CACHE_PROVIDER = "cache.provider";

    private static final String CACHE_REDIS = "redis";


    @Override
    public boolean matches(ConditionContext conditionContext,
        AnnotatedTypeMetadata annotatedTypeMetadata) {
      String cacheProvider = conditionContext.getEnvironment()
          .getProperty(CACHE_PROVIDER);
      if (Strings.isNullOrEmpty(cacheProvider)) {
        return false;
      }
      return Splitter.on(",").omitEmptyStrings()
          .splitToList(cacheProvider).contains(CACHE_REDIS);
    }

  }

}
