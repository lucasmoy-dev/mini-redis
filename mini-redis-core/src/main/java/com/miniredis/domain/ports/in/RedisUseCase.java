package com.miniredis.domain.ports.in;

import java.util.List;

public interface RedisUseCase {
    String set(String key, String value);

    String setEx(String key, String value, long seconds);

    String get(String key);

    Long del(String key);

    Long dbSize();

    Long incr(String key);

    Long zAdd(String key, double score, String member);

    Long zCard(String key);

    Long zRank(String key, String member);

    List<String> zRange(String key, int start, int stop);

    // Helper for Query Param commands
    String executeCommand(String commandLine);
}
