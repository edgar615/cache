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

import com.google.common.base.Preconditions;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 带过期时间的缓存.
 *
 * @author Edgar  Date 2016/5/12
 */
public class ExpiringCache<K, V> implements Cache<K, V> {

  private final Cache<K, V> cache;

  private final DelayQueue<ExpiringKey> delayQueue = new DelayQueue<ExpiringKey>();

  public ExpiringCache(Cache<K, V> cache) {
    Preconditions.checkArgument(!(cache instanceof ExpiringCache));
    this.cache = cache;
  }

  /**
   * 清除过期的缓存.
   */
  public void cleanup() {
    ExpiringKey<K> delayedKey = delayQueue.poll();
    while (delayedKey != null) {
      cache.delete(delayedKey.getKey());
      delayedKey = delayQueue.poll();
    }
  }

  @Override
  public V get(K key) {
    cleanup();
    return cache.get(key);
  }

  @Override
  public void put(K key, V value) {
    cleanup();
    cache.put(key, value);
  }

  @Override
  public void put(K key, V value, long expires) {
    cache.put(key, value);
    delayQueue.put(new ExpiringKey(key, expires));
  }

  @Override
  public void delete(K key) {
    cleanup();
    cache.delete(key);
  }

  @Override
  public void addEvictionListener(EvictionListener<K, V> listener) {
    this.cache.addEvictionListener(listener);
  }

  private class ExpiringKey<K> implements Delayed {

    private final long maxLifeTimeMillis;

    private final K key;

    private long startTime = System.currentTimeMillis();

    public ExpiringKey(K key, long maxLifeTimeMillis) {
      this.maxLifeTimeMillis = maxLifeTimeMillis;
      this.key = key;
    }

    public void renew() {
      startTime = System.currentTimeMillis();
    }

    public void expire() {
      startTime = System.currentTimeMillis() - maxLifeTimeMillis - 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
      int hash = 7;
      hash = 31 * hash + (this.key != null ? this.key.hashCode() : 0);
      return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final ExpiringKey<K> other = (ExpiringKey<K>) obj;
      if (this.key != other.key && (this.key == null || !this.key.equals(other.key))) {
        return false;
      }
      return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getDelay(TimeUnit unit) {
      return unit.convert(getDelayMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Delayed that) {
      return Long.compare(this.getDelayMillis(), ((ExpiringKey) that).getDelayMillis());
    }

    public K getKey() {
      return key;
    }

    private long getDelayMillis() {
      return (startTime + maxLifeTimeMillis) - System.currentTimeMillis();
    }
  }
}
