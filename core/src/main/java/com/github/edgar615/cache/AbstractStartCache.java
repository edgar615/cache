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

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Edgar on 2018/5/18.
 *
 * @author Edgar  Date 2018/5/18
 */
public abstract class AbstractStartCache<ID, T> implements StartCache<ID, T> {

  private final List<T> elements = new CopyOnWriteArrayList<>();

  private final Function<T, ID> idFunction;

  private final ReadWriteLock lock;

  protected AbstractStartCache(Function<T, ID> idFunction) {
    Objects.requireNonNull(idFunction);
    this.idFunction = idFunction;
    this.lock = new ReentrantReadWriteLock();
  }

  @Override
  public final void add(List<T> data) {
    Objects.requireNonNull(data);
    Lock writeLock = lock.writeLock();
    try {
      writeLock.lock();
      elements.addAll(data);
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public final void add(T data) {
    Objects.requireNonNull(data);
    add(Lists.newArrayList(data));
  }

  @Override
  public final void update(List<T> data) {
    Objects.requireNonNull(data);
    Lock writeLock = lock.writeLock();
    try {
      delete(data);
      add(data);
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public final void update(T data) {
    Objects.requireNonNull(data);
    update(Lists.newArrayList(data));
  }

  @Override
  public final void delete(List<T> dataList) {
    Objects.requireNonNull(dataList);
    Lock writeLock = lock.writeLock();
    try {
      List<ID> ids = dataList.stream()
          .map(data -> idFunction.apply(data))
          .collect(Collectors.toList());
      elements.removeIf(e -> ids.contains(idFunction.apply(e)));
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public final void delete(T data) {
    Objects.requireNonNull(data);
    delete(Lists.newArrayList(data));
  }

  @Override
  public final List<T> elements() {
    Lock readLock = lock.writeLock();
    try {
      readLock.lock();
      return new ArrayList<>(elements);
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public final T get(ID id) {
    Lock readLock = lock.writeLock();
    try {
      readLock.lock();
      return elements.stream().filter(e -> idFunction.apply(e).equals(id))
          .findFirst().orElse(null);
    } finally {
      readLock.unlock();
    }
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
  public final void reload(List<T> data) {
    Lock writeLock = lock.writeLock();
    try {
      elements.clear();
      elements.addAll(data);
    } finally {
      writeLock.unlock();
    }
  }
}
