package com.github.edgar615.cache.spring.caffeine;

import com.github.edgar615.cache.spring.caffeine.ConditionalOnCaffeine.CaffeineCondition;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(CaffeineCondition.class)
public @interface ConditionalOnCaffeine {

  public static class CaffeineCondition implements Condition {

    private static final String CACHE_PROVIDER = "cache.provider";

    private static final String CACHE_CAFFEINE = "caffeine";


    @Override
    public boolean matches(ConditionContext conditionContext,
        AnnotatedTypeMetadata annotatedTypeMetadata) {
      String cacheProvider = conditionContext.getEnvironment()
          .getProperty(CACHE_PROVIDER);
      if (Strings.isNullOrEmpty(cacheProvider)) {
        return false;
      }
      return Splitter.on(",").omitEmptyStrings()
          .splitToList(cacheProvider).contains(CACHE_CAFFEINE);
    }

  }

}
