package com.github.edgar615.cache.spring.caffeine;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Administrator on 2017/9/20.
 */
@ConfigurationProperties(prefix = "cache.caffeine")
public class CaffeineCacheProperties {

  private Map<String, String> spec = new HashMap<>();

  public Map<String, String> getSpec() {
    return spec;
  }

  public void setSpec(Map<String, String> spec) {
    this.spec = spec;
  }
}
