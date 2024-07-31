package com.asr.example.vert.x.demo.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.LocalDateTime;

public class IdempotencyCache {

  private final Cache<String, LocalDateTime> cache;

  public IdempotencyCache(long defaultExpirationTimeInSeconds) {
    this.cache = Caffeine.newBuilder()
      .expireAfterWrite(defaultExpirationTimeInSeconds, java.util.concurrent.TimeUnit.SECONDS)
      .build();
  }

  public void put(String key) {
    cache.put(key, LocalDateTime.now());
  }

  public void invalidate(String key) {
    cache.invalidate(key);
  }

  public boolean contains(String key) {
    return cache.getIfPresent(key)!=null;
  }

  public void invalidateAll() {
    cache.invalidateAll();
  }
}
