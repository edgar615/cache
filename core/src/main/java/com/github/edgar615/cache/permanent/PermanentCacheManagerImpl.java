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

package com.github.edgar615.cache.permanent;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

/**
 * PermanentCache的管理器实现.
 *
 * @author Edgar  Date 2018/5/22
 */
class PermanentCacheManagerImpl implements PermanentCacheManager {

  private final Collection<? extends PermanentCache> caches;

  private final ScheduledExecutorService scheduledExecutorService;

  PermanentCacheManagerImpl(Collection<? extends PermanentCache> caches) {
    this(caches, null);
  }

  PermanentCacheManagerImpl(Collection<? extends PermanentCache> caches,
      ScheduledExecutorService scheduledExecutorService) {
    Objects.requireNonNull(caches);
    this.scheduledExecutorService = scheduledExecutorService;
    this.caches = caches;
  }

  @Override
  public PermanentCache getCache(String name) {
    Optional<?> optional = this.caches.stream()
        .filter(c -> c.name().equals(name))
        .findFirst();
    if (optional.isPresent()) {
      return (PermanentCache) optional.get();
    }
    return null;
  }

  @Override
  public Collection<String> getCacheNames() {
    return this.caches.stream()
        .map(c -> c.name())
        .collect(Collectors.toList());
  }

  /**
   * 首次加载数据
   */
  @Override
  public void start() {
    for (PermanentCache cache : caches) {
      cache.load();
    }
    if (scheduledExecutorService != null) {
      for (PermanentCache cache : caches) {
        scheduledExecutorService.scheduleAtFixedRate(() -> cache.load(), cache.refreshPolicy().period() / 2, cache.refreshPolicy().period(), cache.refreshPolicy().unit());
      }
    }
  }

  @Override
  public void shutdown() {
    if (scheduledExecutorService != null) {
      scheduledExecutorService.shutdown();
    }
  }
}
