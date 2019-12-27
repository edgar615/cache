package com.github.edgar615.cache.spring.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({CaffeineCacheProperties.class})
public class CaffeineCacheAutoConfiguration {

  @Bean("caffeineCacheManager")
  @ConditionalOnCaffeine
  @ConditionalOnMissingBean(name = "caffeineCacheManager")
  @ConditionalOnClass(com.github.benmanes.caffeine.cache.Cache.class)
  public CacheManager caffeineCacheManager(CaffeineCacheProperties properties) {
    List<Cache> caches = new ArrayList<>();
    List<Cache> caffeine = properties.getSpec().entrySet().stream()
        .map(
            spec -> new WildcardCaffeineCache(spec.getKey(), Caffeine.from(spec.getValue()).build(),
                true))
        .collect(Collectors.toList());
    caches.addAll(caffeine);
    SimpleCacheManager caffeineManager = new SimpleCacheManager();
    caffeineManager.setCaches(caches);
    caffeineManager.initializeCaches();
    return caffeineManager;
  }

}
