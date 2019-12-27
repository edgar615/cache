package com.github.edgar615.cache.spring;

import com.github.edgar615.cache.spring.l2.L2Cache;
import com.github.edgar615.cache.spring.l2.L2CacheProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties({L2CacheProperties.class})
@AutoConfigureAfter(name = {
    "com.github.edgar615.cache.spring.caffeine.CaffeineCacheAutoConfiguration",
    "com.github.edgar615.cache.spring.redis.RedisCacheAutoConfiguration"})
public class CacheAutoConfiguration {

  @Bean("cacheManager")
  @Primary
  @ConditionalOnProperty(name = "cache.provider")
  @ConditionalOnMissingBean(name = "cacheManager")
  public CacheManager cacheManager(L2CacheProperties l2CacheProperties,
      ApplicationContext context) {
    Map<String, CacheManager> cacheManagerMap = context.getBeansOfType(CacheManager.class);
    List<CacheManager> cacheManagers = new ArrayList<>(cacheManagerMap.values());
    List<Cache> l2Cache = l2CacheProperties.getSpec().entrySet().stream()
        .filter(spec -> findByName(cacheManagers, spec.getValue().getL1()) != null)
        .filter(spec -> findByName(cacheManagers, spec.getValue().getL2()) != null)
        .map(spec -> new L2Cache(spec.getKey(),
            findByName(cacheManagers, spec.getValue().getL1()),
            findByName(cacheManagers, spec.getValue().getL2()), true))
        .collect(Collectors.toList());
    if (!l2Cache.isEmpty()) {
      SimpleCacheManager l2CacheManager = new SimpleCacheManager();
      l2CacheManager.setCaches(l2Cache);
      l2CacheManager.initializeCaches();
      cacheManagers.add(l2CacheManager);
    }
    CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
    compositeCacheManager.setCacheManagers(cacheManagers);
    compositeCacheManager.afterPropertiesSet();
    compositeCacheManager.setFallbackToNoOpCache(true);
    return compositeCacheManager;
  }

  @Bean
  @ConditionalOnBean(CacheManager.class)
  public CacheEvictEventListener cacheEvictEventListener(CacheManager cacheManager) {
    return new CacheEvictEventListener(cacheManager);
  }

  private Cache findByName(Collection<CacheManager> cacheManagers, String name) {
    return cacheManagers.stream()
        .map(cacheManager -> cacheManager.getCache(name))
        .filter(cache -> cache != null)
        .findFirst()
        .orElse(null);
  }

}
