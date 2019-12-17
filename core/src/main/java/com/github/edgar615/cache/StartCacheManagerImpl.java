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

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * StartCache的管理器实现.
 *
 * @author Edgar  Date 2018/5/22
 */
class StartCacheManagerImpl implements StartCacheManager {

  private final Collection<? extends StartCache> caches;

  StartCacheManagerImpl(Collection<? extends StartCache> caches) {
    Objects.requireNonNull(caches);
    this.caches = caches;
  }

  @Override
  public synchronized StartCache getCache(String name) {
    Optional<?> optional = this.caches.stream()
        .filter(c -> c.name().equals(name))
        .findFirst();
    if (optional.isPresent()) {
      return (StartCache) optional.get();
    }
    return null;
  }

  @Override
  public synchronized Collection<String> getCacheNames() {
    return this.caches.stream()
        .map(c -> c.name())
        .collect(Collectors.toList());
  }

  /**
   * 首次加载数据
   */
  @Override
  public void start() {
    for (StartCache cache : caches) {
      cache.load();
    }
  }
}
