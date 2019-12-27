package com.github.edgar615.cache.spring.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = {"com.github.edgar615.**"})//扫描jar
@EnableCaching
@EnableAspectJAutoProxy
@Configuration
public class Application {

  @Autowired
  private CacheService cacheService;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
