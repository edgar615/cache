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

import org.assertj.core.api.Assertions;

/**
 * Created by edgar on 16-4-1.
 */
public class LRUCacheTest {

  public static void main(String[] args) {
    LRUCache<String, String> cache = LRUCache.<String, String>builder().setMaxSize(2).build();
    cache.put("a", "a");
    cache.put("b", "b");
    Assertions.assertThat(cache.get("a")).isEqualTo("a");
    Assertions.assertThat(cache.get("b")).isEqualTo("b");
    cache.get("a");
    cache.get("a");
    cache.get("a");
    cache.get("a");
    cache.put("c", "c");
    Assertions.assertThat(cache.get("a")).isEqualTo("a");
    Assertions.assertThat(cache.get("b")).isNull();
    Assertions.assertThat(cache.get("c")).isEqualTo("c");

    System.out.println(cache);

    System.out.println(cache.getHitRate());

    cache.delete("c");
    Assertions.assertThat(cache.get("c")).isNull();

    System.out.println(cache);
    System.out.println(cache.getHitRate());

    cache.addEvictionListener(new EvictionListener<String, String>() {
      @Override
      public void evicted(String key, String value) {
        System.out.println(key + ":" + value);
      }
    });
    cache.delete("c");
    cache.put("c", "c");
    cache.delete("c");
    Assertions.assertThat(cache.get("c")).isNull();
    System.out.println(cache);
    System.out.println(cache.getHitRate());
  }

}
