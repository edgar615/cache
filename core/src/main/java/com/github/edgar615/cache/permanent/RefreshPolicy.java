package com.github.edgar615.cache.permanent;

import java.util.concurrent.TimeUnit;

public class RefreshPolicy {

  private final int period;

  private final TimeUnit unit;

  private RefreshPolicy(int period, TimeUnit unit) {
    this.period = period;
    this.unit = unit;
  }

  public static RefreshPolicy create(int period, TimeUnit unit) {
    return new RefreshPolicy(period, unit);
  }

  public int period() {
    return period;
  }

  public TimeUnit unit() {
    return unit;
  }
}
