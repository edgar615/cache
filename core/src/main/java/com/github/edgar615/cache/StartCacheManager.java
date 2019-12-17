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

/**
 * StartCache的管理器实现.
 *
 * @author Edgar  Date 2018/5/22
 */
public interface StartCacheManager {

  /**
   * 根据cache名称返回cache
   * @param name cache名称
   * @return cache
   */
  StartCache getCache(String name);

  /**
   * 返回所有的cache列表
   * @return
   */
  Collection<String> getCacheNames();

  /**
   * 启动StartCache，从数据源拉取数据到内存
   */
  void start();

  static StartCacheManager create(Collection<? extends StartCache> caches) {
    return new StartCacheManagerImpl(caches);
  }
}
