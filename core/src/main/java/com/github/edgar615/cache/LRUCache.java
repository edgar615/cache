/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.edgar615.cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * LRU缓存. 键最近最少使用的数据删除.
 *
 * @author Edgar
 */
public class LRUCache<K, V> implements Cache<K, V> {

  /**
   * 缓存名
   */
  private final String name;

  /**
   * 存放数据的map
   */
  private final Map<K, V> map;

  /**
   * 统计请求数率
   */
  private final AtomicLong accesses = new AtomicLong(0);

  /**
   * 统计未命中率
   */
  private final AtomicLong misses = new AtomicLong(0);

  private EvictionListener<K, V> listener = (k, v) -> {
  };

  public LRUCache(String name, int maxSize, float loadFactor, boolean makeSynchronized) {
    this.name = name;
    //避免扩容
    int capacity = (int) Math.ceil(maxSize / loadFactor) + 1;
    Map<K, V> linkedHashMap = new LinkedHashMap<K, V>(capacity, loadFactor, true) {
      @Override
      protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > maxSize;
      }


    };
    if (makeSynchronized) {
      map = Collections.synchronizedMap(linkedHashMap);
    } else {
      map = linkedHashMap;
    }
  }

  public static <K, V> Builder<K, V> builder() {
    return new Builder<>();
  }

  @Override
  public V get(K key) {
    accesses.incrementAndGet();
    V value = map.get(key);
    if (value == null) {
      misses.incrementAndGet();
    }
    return value;

  }

  @Override
  public void put(K key, V value) {
    map.put(key, value);
  }

  @Override
  public void put(K key, V value, long expires) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void delete(K key) {
    V value = map.remove(key);
    if (value != null) {
      listener.evicted(key, value);
    }
  }

  @Override
  public void addEvictionListener(EvictionListener<K, V> listener) {
    if (listener != null) {
      this.listener = listener;
    }
  }

  @Override
  public String toString() {
    return String.format("size: %d, accesses: %s, misses: %s",
        map.size(),
        accesses,
        misses);
  }

  public long getAccesses() {
    return accesses.longValue();
  }

  public long getMisses() {
    return misses.longValue();
  }

  public double getHitRate() {
    double numAccesses = accesses.longValue();
    return numAccesses == 0 ? 0 : (numAccesses - misses.longValue()) / numAccesses;
  }

  public static class Builder<K, V> {

    private String name = null;

    private int maxSize = 1000;

    private float loadFactor = 0.75f;

    private boolean makeSynchronized = true;

    public LRUCache<K, V> build() {
      return new LRUCache<>(name, maxSize, loadFactor, makeSynchronized);
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setMaxSize(int maxSize) {
      this.maxSize = maxSize;
      return this;
    }

    public Builder setLoadFactor(float loadFactor) {
      this.loadFactor = loadFactor;
      return this;
    }

    public Builder setMakeSynchronized(boolean makeSynchronized) {
      this.makeSynchronized = makeSynchronized;
      return this;
    }
  }
}
