package com.github.edgar615.cache.spring.l2;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Administrator on 2017/9/20.
 */
@ConfigurationProperties(prefix = "cache.l2-cache")
public class L2CacheProperties {

  private Map<String, L2CacheSpec> spec = new HashMap<>();

  public Map<String, L2CacheSpec> getSpec() {
    return spec;
  }

  public void setSpec(
      Map<String, L2CacheSpec> spec) {
    this.spec = spec;
  }
}
