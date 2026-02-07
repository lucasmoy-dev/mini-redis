package com.miniredis.domain.ports.out;

import java.util.List;
import java.util.Optional;

public interface RedisRepository {
    void set(String key, String value, Long expiryTimeMillis);

    Optional<String> get(String key);

    boolean delete(String key);

    long databaseSize();

    void flushAll();

    long increment(String key);

    long zAdd(String key, double score, String member);

    long zCard(String key);

    Optional<Long> zRank(String key, String member);

    List<String> zRange(String key, int start, int stop);

    void cleanupExpired();
}
