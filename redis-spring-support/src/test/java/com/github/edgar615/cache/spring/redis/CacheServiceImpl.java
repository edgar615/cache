package com.github.edgar615.cache.spring.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService, ApplicationEventPublisherAware {

  private ApplicationEventPublisher publisher;

  private final Map<String, AtomicInteger> stat = new HashMap<>();

  private void stat(String method) {
    if (stat.get(method) == null) {
      AtomicInteger integer = new AtomicInteger();
      integer.incrementAndGet();
      stat.put(method, integer);
    } else {
      stat.get(method).incrementAndGet();
    }
  }
  @Override
  @Cacheable(cacheNames = "redisCache1", key = "#p0")
  public String getCache1(int id) {
    System.out.println("cache1");
    stat("getCache1");
    return UUID.randomUUID().toString();
  }

  @Override
  @Cacheable(cacheNames = "redisCache2", key = "#p0")
  public String getCache2(int id) {
    System.out.println("cache2");
    stat("getCache2");
    return UUID.randomUUID().toString();
  }

  @Override
  public int count(String method) {
    return stat.getOrDefault(method, new AtomicInteger()).get();
  }

  @Override
  public void clearStat() {
    stat.clear();
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.publisher = applicationEventPublisher;
  }

  public Map<String, AtomicInteger> getStat() {
    return stat;
  }
}
