package com.github.edgar615.cache.permanent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MockCacheLoader implements PermanentCacheLoader<User> {

  private final CopyOnWriteArrayList<User> userList;

  public MockCacheLoader(CopyOnWriteArrayList<User> userList) {
    this.userList = userList;
  }

  @Override
  public List<User> load() {
    System.out.println(System.currentTimeMillis());
    return new ArrayList<>(userList);
  }
}
