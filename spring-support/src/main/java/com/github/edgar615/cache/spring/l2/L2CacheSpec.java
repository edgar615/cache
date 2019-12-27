package com.github.edgar615.cache.spring.l2;

/**
 * 二级缓存的配置
 */
public class L2CacheSpec {
  private String l1;

  private String l2;

  public String getL1() {
    return l1;
  }

  public void setL1(String l1) {
    this.l1 = l1;
  }

  public String getL2() {
    return l2;
  }

  public void setL2(String l2) {
    this.l2 = l2;
  }
}
