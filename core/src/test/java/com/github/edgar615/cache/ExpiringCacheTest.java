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

import java.util.concurrent.TimeUnit;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Created by edgar on 16-4-1.
 */
public class ExpiringCacheTest {

  @Test
  public void testCache() {
    ExpiringCache<String, String> cache =
        new ExpiringCache<>(LRUCache.<String, String>builder().setMaxSize(2).build());

    cache.put("a", "a", 1000);
    cache.put("b", "b", 2000);
    Assertions.assertThat(cache.get("a")).isEqualTo("a");
    Assertions.assertThat(cache.get("b")).isEqualTo("b");
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Assertions.assertThat(cache.get("a")).isNull();
    Assertions.assertThat(cache.get("b")).isEqualTo("b");
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    Assertions.assertThat(cache.get("b")).isNull();
  }

}
