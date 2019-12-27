package com.github.edgar615.cache;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Assert;
import org.junit.Test;

public class CacheWatingQueueTest {

  @Test
  public void testConcurrent() throws InterruptedException {
    AtomicInteger count = new AtomicInteger();
    CacheWaitingQueue<Integer, String> queue = new CacheWaitingQueue<>(num -> {
      count.incrementAndGet();
      return value(num);
    });
    CountDownLatch countDownLatch = new CountDownLatch(10);
    Set<String> result = new HashSet<>();

    for (int i = 0; i < 30; i ++) {
      new Thread(() -> {
        result.add(queue.get(1));
        countDownLatch.countDown();
      }).start();
    }
    countDownLatch.await();
//    cacheService.getConcurrent(1);
    System.out.println(count);
    System.out.println(result.size());
    Assert.assertTrue(count.get() < 30);
  }

  private String value(Integer num) {
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return UUID.randomUUID().toString();
  }

}
