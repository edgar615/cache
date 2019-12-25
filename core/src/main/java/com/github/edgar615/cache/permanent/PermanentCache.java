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

import java.util.List;

/**
 * 永远常驻内存的缓存，主要用来存放基本不会变动的基础数据.
 *
 * @author Edgar  Date 2018/5/18
 */
public interface PermanentCache<ID, T> {

  static <ID, T> PermanentCacheBuilder<ID, T> builder() {
    return new PermanentCacheBuilder<>();
  }

  /**
   * 缓存的名称
   *
   * @return
   */
  String name();

  /**
   * 刷新策略
   *
   * @return
   */
  RefreshPolicy refreshPolicy();

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
   * 根据ID查询
   * <b>元素如果发生更改会引起数据不一致</b>
   *
   * @param id
   * @return
   */
  T get(ID id);

//  /**
//   * 将map对象转换实体.
//   *
//   * 这个方法主要是结合消息通知更新进程内缓存的一个辅助方法.
//   *
//   * @param source 源数据
//   * @return 转换后的对象
//   */
//  T transform(Map<String, Object> source);

  /**
   * 加载data中的数据
   */
  void load();

}
