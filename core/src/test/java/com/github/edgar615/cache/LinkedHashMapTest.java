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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by edgar on 16-4-1.
 */
public class LinkedHashMapTest {

  public static void main(String[] args) {
    //LinkedHashMap的一个构造函数，当参数accessOrder为true时，即会按照访问顺序排序，最近访问的放在最前，最早访问的放在后面
    Map<String, String> map = new LinkedHashMap<>();
    map.put("a", "a");
    map.put("b", "b");
    map.get("b");
    System.out.println(map); //{a=a, b=b}
    map.get("b");
    map.get("b");
    map.get("b");
    System.out.println(map);//{a=a, b=b}

    //每次添加都会删除
    map = new LinkedHashMap<String, String>() {
      @Override
      protected boolean removeEldestEntry(Map.Entry eldest) {
        return true;
      }
    };
    map.put("a", "a");
    System.out.println(map); //{}
    map.put("b", "b");
    System.out.println(map); //{}

    //始终删除最早的
    map = new LinkedHashMap<String, String>(2, 0.75f, false) {
      @Override
      protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > 2;
      }
    };
    map.put("a", "a");
    map.put("b", "b");
    System.out.println(map); //{a=a, b=b}
    map.get("a");
    map.get("a");
    map.get("a");
    map.get("a");
    map.put("c", "c");
    System.out.println(map); //{b=b, c=c}

    //始终删除访问最少的
    map = new LinkedHashMap<String, String>(2, 0.75f, true) {
      @Override
      protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > 2;
      }
    };
    map.put("a", "a");
    map.put("b", "b");
    System.out.println(map); //{a=a, b=b}
    map.get("a");
    map.get("a");
    map.get("a");
    map.get("a");
    map.put("c", "c");
    System.out.println(map); //{a=a, c=c}

  }

}
