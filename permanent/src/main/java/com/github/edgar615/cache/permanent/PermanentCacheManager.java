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
import java.util.concurrent.ScheduledExecutorService;

/**
 * PermanentCache的管理器实现.
 *
 * @author Edgar  Date 2018/5/22
 */
public interface PermanentCacheManager {

  /**
   * 根据cache名称返回cache
   * @param name cache名称
   * @return cache
   */
  PermanentCache getCache(String name);

  /**
   * 返回所有的cache列表
   * @return
   */
  Collection<String> getCacheNames();

  /**
   * 启动PermanentCache，从数据源拉取数据到内存
   */
  void start();

  /**
   * 关闭PermanentCache，关闭定时器
   */
  void shutdown();

  static PermanentCacheManager create(Collection<? extends PermanentCache> caches) {
    return new PermanentCacheManagerImpl(caches);
  }

  static PermanentCacheManager create(Collection<? extends PermanentCache> caches,
      ScheduledExecutorService scheduledExecutorService) {
    return new PermanentCacheManagerImpl(caches, scheduledExecutorService);
  }
}
