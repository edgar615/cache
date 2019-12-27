package com.github.edgar615.cache.spring.redis;

public interface CacheService {
  String getCache1(int id);

  String getCache2(int id);

  int count(String method);

  void clearStat();
}
