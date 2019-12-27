package com.github.edgar615.cache.spring;

import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;

public class CacheWaitingQueue {

  private final WeakHashMap<Object, CompletableFuture<Object>> queue = new WeakHashMap<>();

  public synchronized CompletableFuture<Object> putFuture(Object key, CompletableFuture<Object> future) {
    return queue.putIfAbsent(key, future);
  }

  public synchronized void removeFuture(Object key, CompletableFuture<Object> future) {
    queue.remove(key, future);
  }

}
