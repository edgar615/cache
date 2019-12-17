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

/**
 * 缓存的接口.
 *
 * 这个接口仅仅是用来模拟实现cache，实际上各种开源框架已经有了很完美的cache实现。所以在实际工作中不建议用这个cache
 *
 * @author Edgar
 */
public interface Cache<K, V> {

  /**
   * 从缓存中获取值.
   *
   * @param key 缓存的键值，不能为 {@code null}.
   * @return 缓存值，如果缓存不存在，返回{@code null}.
   */
  V get(K key);

  /**
   * 将键值对放入缓存.
   *
   * @param key 缓存的键值，不能为 {@code null}.
   * @param value 缓存的键值，不能为 {@code null}.
   */
  void put(K key, V value);

  /**
   * 将键值对放入缓存.
   *
   * @param key 缓存的键值，不能为 {@code null}.
   * @param value 缓存的键值，不能为 {@code null}.
   * @param expires 缓存的失效
   */
  void put(K key, V value, long expires);

  /**
   * 根据键删除缓存值.
   *
   * @param key 缓存的键值，不能为 {@code null}.
   */
  void delete(K key);

  /**
   * 增加监听器，当从缓存中删除值的时候触发.
   *
   * @param listener 监听器
   */
  void addEvictionListener(EvictionListener<K, V> listener);

}
