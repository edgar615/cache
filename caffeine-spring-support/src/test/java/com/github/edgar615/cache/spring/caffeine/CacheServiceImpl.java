package com.github.edgar615.cache.spring.caffeine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

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
  @Cacheable(cacheNames = "caffeineCache1", key = "#p0")
  public String getCache1(int id) {
    System.out.println("cache1");
    stat("getCache1");
    return UUID.randomUUID().toString();
  }

  @Override
  @Cacheable(cacheNames = "caffeineCache2", key = "#p0")
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
  @Cacheable(cacheNames = "caffeineCache3", key = "#p0")
  public String getConcurrent(int id) {
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(id);
    System.out.println("cache3:concurrent");
    stat("getConcurrent");
    return UUID.randomUUID().toString();
  }

  public Map<String, AtomicInteger> getStat() {
    return stat;
  }
}
