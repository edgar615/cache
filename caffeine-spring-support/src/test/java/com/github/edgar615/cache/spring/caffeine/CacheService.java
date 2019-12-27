package com.github.edgar615.cache.spring.caffeine;

public interface CacheService {
  String getCache1(int id);

  String getCache2(int id);

  int count(String method);

  void clearStat();

  String getConcurrent(int id);
}
