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
import java.util.Map;

/**
 * 启动启动时加载到内存中的缓存.
 * 实现一个定时任务，定时执行load刷新数据
 *
 * @author Edgar  Date 2018/5/18
 */
public interface StartCache<ID, T> {

  /**
   * 缓存的名称
   *
   * @return
   */
  String name();

  /**
   * 清除缓存
   */
  void clear();

  /**
   * 查询列表.
   * <b>数组中的元素如果发生更改会引起数据不一致</b>
   *
   * @return
   */
  List<T> elements();

  /**
   * 初始化动作.
   */
  void load();

  /**
   * 根据ID查询
   * <b>元素如果发生更改会引起数据不一致</b>
   *
   * @param id
   * @return
   */
  T get(ID id);

  /**
   * 增加数据
   *
   * @param data
   */
  void add(List<T> data);

  /**
   * 增加数据
   *
   * @param data
   */
  void add(T data);

  /**
   * 修改数据
   *
   * @param data
   */
  void update(List<T> data);

  /**
   * 修改数据
   *
   * @param data
   */
  void update(T data);

  /**
   * 删除数据
   *
   * @param data
   */
  void delete(List<T> data);

  /**
   * 删除数据
   *
   * @param data
   */
  void delete(T data);

  /**
   * 将map对象转换实体.
   *
   * 这个方法主要是结合消息通知更新进程内缓存的一个辅助方法.
   *
   * @param source 源数据
   * @return 转换后的对象
   */
  T transform(Map<String, Object> source);

  /**
   * 全量替换data中的数据
   * @param data
   */
  void reload(List<T> data);
}
