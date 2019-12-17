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

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 封装了将StartCache重新加载的核销逻辑
 * @param <ID> 主键
 * @param <T> 数据
 */
public abstract class AbstractStartCacheReloadAction<ID, T> implements
    StartCacheReloadAction<ID, T> {

  private final StartCache<ID, T> startCache;

  private final Function<T, ID> idFunction;

  protected AbstractStartCacheReloadAction(
      StartCache<ID, T> startCache, Function<T, ID> idFunction) {
    this.startCache = startCache;
    this.idFunction = idFunction;
  }

  private ID getId(T data) {
    return idFunction.apply(data);
  }

  @Override
  public void execute() {
    List<T> categories = load();
    // 没有使用锁
    List<T> deletedList = startCache.elements()
        .stream().filter(categoryInCache -> categories.stream().noneMatch(
            category -> getId(category).equals(getId(categoryInCache))))
        .collect(Collectors.toList());
    List<T> updatedList = categories.stream()
        .filter(category -> startCache.elements().stream().anyMatch(
            categoryInCache -> getId(category).equals(getId(categoryInCache))))
        .collect(Collectors.toList());
    List<T> insertedList = categories.stream()
        .filter(category -> startCache.elements().stream().noneMatch(
            categoryInCache -> getId(category).equals(getId(categoryInCache))))
        .collect(Collectors.toList());
    startCache.delete(deletedList);
    startCache.update(updatedList);
    startCache.add(insertedList);
  }

  public abstract List<T> load();
}
