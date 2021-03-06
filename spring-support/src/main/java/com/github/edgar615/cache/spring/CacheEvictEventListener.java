package com.github.edgar615.cache.spring;

import com.github.edgar615.cache.spring.l2.L2Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationListener;

/**
 * 驱逐缓存事件监听器.
 *
 * @author Edgar  Date 2018/8/17
 */
public class CacheEvictEventListener implements ApplicationListener<CacheEvictEvent> {

  private final Logger logger = LoggerFactory.getLogger(CacheEvictEventListener.class);

  private final CacheManager cacheManager;

  public CacheEvictEventListener(CacheManager cacheManager) {this.cacheManager = cacheManager;}

  @Override
  public void onApplicationEvent(CacheEvictEvent event) {
    CacheEvictMessage message = (CacheEvictMessage) event.getSource();
    Cache cache = cacheManager.getCache(message.cacheName());
    if (cache == null) {
      logger.debug("no cache can be evict : {}", message.cacheName());
      return;
    }
    if (cache instanceof L2Cache) {
      evictL2Cache(message, (L2Cache) cache);
    } else {
      evictCache(message, cache);
    }

  }

  private void evictCache(CacheEvictMessage message, Cache cache) {
    if (message.allEntries()) {
      logger.debug("clear cache: {}", message.cacheName());
      cache.clear();
    }
    if (message.key() != null) {
      logger.debug("evict cache: {}, {}", message.cacheName(), message.key());
      cache.evict(message.key());
    }
  }

  private void evictL2Cache(CacheEvictMessage message, L2Cache cache) {
    L2Cache l2Cache = cache;
    if (message.allEntries()) {
      logger.debug("clear cache: {}", message.cacheName());
      l2Cache.clearL1();
    }
    if (message.key() != null) {
      logger.debug("evict cache: {}, {}", message.cacheName(), message.key());
      l2Cache.evictL1(message.key());
    }
  }
}
