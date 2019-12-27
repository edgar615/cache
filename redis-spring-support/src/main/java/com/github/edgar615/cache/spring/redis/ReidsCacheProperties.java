package com.github.edgar615.cache.spring.redis;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache.redis")
public class ReidsCacheProperties {

  private Map<String, RedisCacheSpec> spec = new HashMap<>();

  public Map<String, RedisCacheSpec> getSpec() {
    return spec;
  }

  public void setSpec(
      Map<String, RedisCacheSpec> spec) {
    this.spec = spec;
  }

}
