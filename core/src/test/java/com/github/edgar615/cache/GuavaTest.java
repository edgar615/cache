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

import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;

/**
 * Created by Edgar on 2016/5/12.
 *
 * @author Edgar  Date 2016/5/12
 */
public class GuavaTest {

  public static void main(String[] args) {

    com.google.common.cache.Cache<String, String> cache = CacheBuilder.newBuilder()
        .expireAfterWrite(5L, TimeUnit.MINUTES)
        .maximumSize(5000L)
        .ticker(Ticker.systemTicker())
        .build();

    System.out.println(cache.getIfPresent("foo"));
    cache.put("foo", "bar");
    System.out.println(cache.getIfPresent("foo"));
    try {
      TimeUnit.SECONDS.sleep(6);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(cache.getIfPresent("foo"));
  }
}
