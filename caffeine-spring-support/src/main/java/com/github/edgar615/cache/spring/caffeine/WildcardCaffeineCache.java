package com.github.edgar615.cache.spring.caffeine;

import org.springframework.cache.caffeine.CaffeineCache;

/**
 * 为了兼容redis的通配符删除，实际上这里的通配符依然会删除所有
 */
public class WildcardCaffeineCache extends CaffeineCache {

  private static final String WILDCARD = "wildcard:";

  com.github.benmanes.caffeine.cache.Cache<Object, Object> cache;

  public WildcardCaffeineCache(String name, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache,
      boolean allowNullValues) {
    super(name, cache, allowNullValues);
    this.cache = cache;
  }

  @Override
  public void evict(Object key) {
    if ((key instanceof String) && key.toString().startsWith(WILDCARD)) {
      super.clear();
    } else {
      super.evict(key);
    }
  }

}
