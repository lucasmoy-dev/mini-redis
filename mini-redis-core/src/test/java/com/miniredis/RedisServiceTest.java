package com.miniredis;

import com.miniredis.application.services.RedisService;
import com.miniredis.infrastructure.adapters.out.memory.InMemoryRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RedisServiceTest {

    private RedisService redisService;

    @BeforeEach
    void setUp() {
        InMemoryRedisRepository repository = new InMemoryRedisRepository();
        redisService = new RedisService(repository);
    }

    @Test
    void testBasicCommands() {
        assertEquals("OK", redisService.set("key1", "val1"));
        assertEquals("val1", redisService.get("key1"));
        assertEquals(1L, redisService.del("key1"));
        assertEquals("(nil)", redisService.get("key1"));
    }

    @Test
    void testIncr() {
        assertEquals(1L, redisService.incr("counter"));
        assertEquals(2L, redisService.incr("counter"));
        assertEquals("2", redisService.get("counter"));
    }

    @Test
    void testTTL() throws InterruptedException {
        redisService.setEx("key", "val", 1);
        assertEquals("val", redisService.get("key"));
        Thread.sleep(1100);
        assertEquals("(nil)", redisService.get("key"));
    }

    @Test
    void testSortedSets() {
        redisService.zAdd("myset", 1.0, "a");
        redisService.zAdd("myset", 3.0, "c");
        redisService.zAdd("myset", 2.0, "b");

        assertEquals(3L, redisService.zCard("myset"));
        assertEquals(0L, redisService.zRank("myset", "a"));
        assertEquals(1L, redisService.zRank("myset", "b"));
        assertEquals(2L, redisService.zRank("myset", "c"));

        List<String> range = redisService.zRange("myset", 0, -1);
        assertEquals(List.of("a", "b", "c"), range);
    }

    @Test
    void testExecuteCommand() {
        assertEquals("OK", redisService.executeCommand("SET key val"));
        assertEquals("val", redisService.executeCommand("GET key"));
        assertEquals("1", redisService.executeCommand("INCR cnt"));
        assertEquals("2", redisService.executeCommand("INCR cnt"));
        assertEquals("1", redisService.executeCommand("ZADD z 10 m1"));
        assertEquals("1", redisService.executeCommand("ZCARD z"));
        assertEquals("m1", redisService.executeCommand("ZRANGE z 0 -1"));
    }
}
