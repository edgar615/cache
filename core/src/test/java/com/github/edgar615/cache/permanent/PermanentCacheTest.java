package com.github.edgar615.cache.permanent;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;

public class PermanentCacheTest {

  @Test
  public void testCache() throws InterruptedException {

    CopyOnWriteArrayList<User> userList = new CopyOnWriteArrayList<>();
    MockCacheLoader loader = new MockCacheLoader(userList);
    PermanentCache<Long, User> permanentCache = PermanentCache.<Long, User>builder()
        .setIdFunction(user -> user.getUserId())
        .setLoader(loader)
        .setName("testPerm")
        .setRefreshPolicy(RefreshPolicy.create(2, TimeUnit.SECONDS))
        .build();
    PermanentCacheManager permanentCacheManager = PermanentCacheManager.create(Lists.newArrayList(permanentCache));
    permanentCacheManager.start();
    Assert.assertTrue(permanentCacheManager.getCache("testPerm").elements().size() == 0);

    userList.add(new User(1L, "user1"));
    userList.add(new User(2L, "user2"));

    TimeUnit.SECONDS.sleep(3);

    Assert.assertTrue(permanentCacheManager.getCache("testPerm").elements().size() == 0);

  }

  @Test
  public void testCacheReFresh() throws InterruptedException {

    CopyOnWriteArrayList<User> userList = new CopyOnWriteArrayList<>();
    MockCacheLoader loader = new MockCacheLoader(userList);
    PermanentCache<Long, User> permanentCache = PermanentCache.<Long, User>builder()
        .setIdFunction(user -> user.getUserId())
        .setLoader(loader)
        .setName("testPerm")
        .setRefreshPolicy(RefreshPolicy.create(2, TimeUnit.SECONDS))
        .build();

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    PermanentCacheManager permanentCacheManager = PermanentCacheManager.create(Lists.newArrayList(permanentCache), executorService);
    permanentCacheManager.start();
    Assert.assertTrue(permanentCacheManager.getCache("testPerm").elements().size() == 0);

    userList.add(new User(1L, "user1"));
    userList.add(new User(2L, "user2"));

    TimeUnit.SECONDS.sleep(3);

    Assert.assertTrue(permanentCacheManager.getCache("testPerm").elements().size() == 2);

  }
}
