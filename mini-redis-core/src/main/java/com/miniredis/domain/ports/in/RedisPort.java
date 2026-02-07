package com.miniredis.domain.ports.in;

import java.util.List;

public interface RedisPort {
    String set(String key, String value);

    String setWithExpiry(String key, String value, long seconds);

    String get(String key);

    Long delete(String key);

    Long databaseSize();

    Long increment(String key);

    Long zAdd(String key, double score, String member);

    Long zCard(String key);

    Long zRank(String key, String member);

    List<String> zRange(String key, int start, int stop);

    String executeCommand(String commandLine);

    void flushAll();
}
