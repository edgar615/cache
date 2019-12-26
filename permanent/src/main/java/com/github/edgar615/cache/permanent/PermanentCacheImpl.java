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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * Created by Edgar on 2018/5/18.
 *
 * @author Edgar  Date 2018/5/18
 */
class PermanentCacheImpl<ID, T> implements PermanentCache<ID, T> {

  private final List<T> elements = new CopyOnWriteArrayList<>();

  private final String name;

  private final Function<T, ID> idFunction;

  private final PermanentCacheLoader<T> loader;

  private final RefreshPolicy refreshPolicy;

  private final ReadWriteLock lock;

  PermanentCacheImpl(String name, Function<T, ID> idFunction,
      PermanentCacheLoader<T> loader,
      RefreshPolicy refreshPolicy) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(idFunction);
    Objects.requireNonNull(loader);
    Objects.requireNonNull(refreshPolicy);
    this.name = name;
    this.idFunction = idFunction;
    this.loader = loader;
    this.refreshPolicy = refreshPolicy;
    this.lock = new ReentrantReadWriteLock();
  }

  @Override
  public final List<T> elements() {
    Lock readLock = lock.readLock();
    try {
      readLock.lock();
      return new ArrayList<>(elements);
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public final T get(ID id) {
    Lock readLock = lock.readLock();
    try {
      readLock.lock();
      return elements.stream().filter(e -> idFunction.apply(e).equals(id))
          .findFirst().orElse(null);
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public RefreshPolicy refreshPolicy() {
    return refreshPolicy;
  }

  @Override
  public final void clear() {
    Lock writeLock = lock.writeLock();
    try {
      this.elements.clear();
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public final void load() {
    Lock writeLock = lock.writeLock();
    try {
      writeLock.lock();
      List<T> data = loader.load();
      elements.clear();
      elements.addAll(data);
    } finally {
      writeLock.unlock();
    }
  }
}
