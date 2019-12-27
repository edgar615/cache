package com.github.edgar615.cache.spring.redis;

/**
 * Redis的缓存配置.
 *
 * @author Edgar  Date 2018/8/15
 */
public class RedisCacheSpec {

  /**
   * 单位毫秒
   */
  private long timeToLive;

  private boolean cacheNullValues;

  private String keyPrefix;

  private boolean useKeyPrefix;

  public long getTimeToLive() {
    return timeToLive;
  }

  public void setTimeToLive(long timeToLive) {
    this.timeToLive = timeToLive;
  }

  public boolean isCacheNullValues() {
    return cacheNullValues;
  }

  public void setCacheNullValues(boolean cacheNullValues) {
    this.cacheNullValues = cacheNullValues;
  }

  public String getKeyPrefix() {
    return keyPrefix;
  }

  public void setKeyPrefix(String keyPrefix) {
    this.keyPrefix = keyPrefix;
  }

  public boolean isUseKeyPrefix() {
    return useKeyPrefix;
  }

  public void setUseKeyPrefix(boolean useKeyPrefix) {
    this.useKeyPrefix = useKeyPrefix;
  }

}
