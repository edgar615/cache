package com.github.edgar615.cache.spring.redis;

import java.util.concurrent.TimeUnit;
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
    cacheManager.getCache("redisCache1").clear();
    cacheManager.getCache("redisCache2").clear();
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

}
