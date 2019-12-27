package com.github.edgar615.cache;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 同一个key一次只放一个去查询，其他等待
 *
// * 尝试用ConcurrentMap<K, CompletableFuture<V>>.compute/computeIfAbsent方法都不理想，在并发环境下依然会执行
 */
public class CacheWaitingQueue<K, V> {

  private final ConcurrentMap<K, CompletableFuture<V>> waitingQueue;

  private final Function<K, V> loader;

  public CacheWaitingQueue(Function<K, V> loader) {
    this.waitingQueue = new ConcurrentHashMap<>();
    this.loader = loader;
  }

  public V get(K key) {
    // 使用同步调用，不用CompletableFuture的异步调用
    BiFunction<K, CompletableFuture<V>, CompletableFuture<V>> mappingFunction = (k, f) -> {
      if (f != null) {
        return f;
      }
      try {
        System.out.println(f);
        V valueWrapper = this.loader.apply(k);
        return CompletableFuture.completedFuture(valueWrapper);
      } catch (Exception e) {
        CompletableFuture<V> completableFuture = new CompletableFuture<>();
        completableFuture.completeExceptionally(e);
        return completableFuture;
      }
    };
    CompletableFuture<V> future = waitingQueue.compute(key, mappingFunction);
    try {
      V valueWrapper = future.get();
      return valueWrapper;
    } catch (ExecutionException e) {
      if (e.getCause() instanceof RuntimeException) {
        throw (RuntimeException) e.getCause();
      } else if (e.getCause() instanceof Error) {
        throw (Error) e.getCause();
      }
      throw new CompletionException(e.getCause());
    } catch (InterruptedException e) {
      throw new CompletionException(e);
    } finally {
      waitingQueue.remove(key, future);
    }
  }
}
