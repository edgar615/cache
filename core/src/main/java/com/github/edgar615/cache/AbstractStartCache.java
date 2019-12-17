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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
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

  protected AbstractStartCache(Function<T, ID> idFunction) {
    Objects.requireNonNull(idFunction);
    this.idFunction = idFunction;
  }

  @Override
  public synchronized void add(List<T> data) {
    Objects.requireNonNull(data);
    elements.addAll(data);
  }

  @Override
  public synchronized void add(T data) {
    Objects.requireNonNull(data);
    elements.add(data);
  }

  @Override
  public synchronized void update(List<T> data) {
    delete(data);
    add(data);
  }

  @Override
  public synchronized void update(T data) {
    delete(data);
    add(data);
  }

  @Override
  public synchronized void delete(List<T> dataList) {
    Objects.requireNonNull(dataList);
    List<ID> ids = dataList.stream()
        .map(data -> idFunction.apply(data))
        .collect(Collectors.toList());
    elements.removeIf(e -> ids.contains(idFunction.apply(e)));
  }

  @Override
  public synchronized void delete(T data) {
    Objects.requireNonNull(data);
    elements.removeIf(e -> idFunction.apply(data).equals(idFunction.apply(e)));
  }

  @Override
  public List<T> elements() {
    return new ArrayList<>(elements);
  }

  @Override
  public T get(ID id) {
    return elements.stream().filter(e -> idFunction.apply(e).equals(id))
        .findFirst().orElse(null);
  }

  @Override
  public void clear() {
    this.elements.clear();
  }
}
