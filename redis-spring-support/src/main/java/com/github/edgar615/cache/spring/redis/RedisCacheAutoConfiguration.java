package com.github.edgar615.cache.spring.redis;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@EnableConfigurationProperties({ReidsCacheProperties.class})
public class RedisCacheAutoConfiguration {

  @Bean("redisCacheManager")
  @ConditionalOnRedis
  @ConditionalOnMissingBean(name = "redisCacheManager")
  @ConditionalOnClass(RedisConnectionFactory.class)
  public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory,
      ReidsCacheProperties cacheProperties) {
    Map<String, RedisCacheConfiguration> initialCacheConfigurations = new HashMap<>();
    for (String cacheName : cacheProperties.getSpec().keySet()) {
      RedisCacheConfiguration configuration = redisCacheConfiguration(
          cacheProperties.getSpec().get(cacheName));
      initialCacheConfigurations.put(cacheName, configuration);
    }
    WildcardEvictRedisCacheManager redisCacheManager = new WildcardEvictRedisCacheManager(
        RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
        RedisCacheConfiguration.defaultCacheConfig(), initialCacheConfigurations, false);
    redisCacheManager.initializeCaches();
    return redisCacheManager;
  }

  private RedisCacheConfiguration redisCacheConfiguration(RedisCacheSpec spec) {
    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
    if (spec.getTimeToLive() > 0) {
      config = config.entryTtl(Duration.ofMillis(spec.getTimeToLive()));
    }
    if (spec.getKeyPrefix() != null) {
      config = config.prefixKeysWith(spec.getKeyPrefix());
    }
    if (!spec.isCacheNullValues()) {
      config = config.disableCachingNullValues();
    }
    if (!spec.isUseKeyPrefix()) {
      config = config.disableKeyPrefix();
    }
    return config;
  }

}
