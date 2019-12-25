package com.github.edgar615.cache.permanent;

import java.util.function.Function;

public class PermanentCacheBuilder<ID, T> {

  private String name;
  private Function<T, ID> idFunction;
  private PermanentCacheLoader<T> loader;
  private RefreshPolicy refreshPolicy;

  public PermanentCacheBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public PermanentCacheBuilder setIdFunction(Function<T, ID> idFunction) {
    this.idFunction = idFunction;
    return this;
  }

  public PermanentCacheBuilder setLoader(PermanentCacheLoader<T> loader) {
    this.loader = loader;
    return this;
  }

  public PermanentCacheBuilder setRefreshPolicy(RefreshPolicy refreshPolicy) {
    this.refreshPolicy = refreshPolicy;
    return this;
  }

  public PermanentCacheImpl build() {
    return new PermanentCacheImpl(name, idFunction, loader, refreshPolicy);
  }
}
