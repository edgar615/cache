package com.github.edgar615.cache.spring.caffeine;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTest {

  @Autowired
  private CacheService cacheService;

  @Autowired
  private CacheManager cacheManager;

  @After
  public void tearDown() {
    cacheManager.getCache("caffeineCache1").clear();
    cacheManager.getCache("caffeineCache2").clear();
    cacheService.clearStat();
  }

  @Test
  public void testCache1() throws InterruptedException {
    cacheService.getCache1(1);
    cacheService.getCache1(1);
    TimeUnit.SECONDS.sleep(3);
    cacheService.getCache1(2);
    TimeUnit.SECONDS.sleep(3);
    cacheService.getCache1(1);
    cacheService.getCache1(2);

    int count = cacheService.count("getCache1");
    Assert.assertEquals(3, count);
  }

  @Test
  public void testCache2() throws InterruptedException {
    cacheService.getCache2(1);
    cacheService.getCache2(1);
    TimeUnit.SECONDS.sleep(3);
    cacheService.getCache2(2);
    TimeUnit.SECONDS.sleep(3);
    cacheService.getCache2(1);
    cacheService.getCache2(2);

    int count = cacheService.count("getCache2");
    Assert.assertEquals(2, count);
  }

//  @Test
//  public void testConcurrent() throws InterruptedException {
//    System.out.println(cacheService.getConcurrent(1));
//    CountDownLatch countDownLatch = new CountDownLatch(10);
//    for (int i = 0; i < 10; i ++) {
//      new Thread(() -> {
//        System.out.println(cacheService.getConcurrent(1));
//        countDownLatch.countDown();
//      }).start();
//    }
//    countDownLatch.await();
//
////    cacheService.getConcurrent(1);
//    int count = cacheService.count("getConcurrent");
//    Assert.assertEquals(1, count);
//  }
//
//  @Test
//  public void testConcurrent2() throws InterruptedException {
//    long start = System.currentTimeMillis();
//    CountDownLatch countDownLatch = new CountDownLatch(10);
//    for (int i = 0; i < 10; i ++) {
//      new Thread(() -> {
//        System.out.println(cacheService.getConcurrent(1));
//        countDownLatch.countDown();
//      }).start();
//    }
//    countDownLatch.await();
//    System.out.println(System.currentTimeMillis() - start);
////    cacheService.getConcurrent(1);
//    int count = cacheService.count("getConcurrent");
//    Assert.assertEquals(1, count);
//  }
//
//  @Test
//  public void testConcurrent3() throws InterruptedException {
//    AtomicInteger count = new AtomicInteger();
//    ConcurrentMap<Integer, CompletableFuture<String>> map = new ConcurrentHashMap<>();
//
//    CountDownLatch countDownLatch = new CountDownLatch(10);
//    for (int i = 0; i < 10; i ++) {
//      new Thread(() -> {
//        CompletableFuture<String> future =  map.compute(1, (k, v) -> {
//          if (v != null) {
//            return v;
//          }
//          System.out.println(v);
//          count.incrementAndGet();
//          return CompletableFuture.completedFuture(value(1));
//        });
//        countDownLatch.countDown();
////        map.remove(1, future);
//      }).start();
//    }
//    countDownLatch.await();
////    cacheService.getConcurrent(1);
//    Assert.assertEquals(1, count.get());
//  }
//
//  private String value(Integer num) {
//    try {
//      TimeUnit.SECONDS.sleep(3);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
//    return UUID.randomUUID().toString();
//  }

}
