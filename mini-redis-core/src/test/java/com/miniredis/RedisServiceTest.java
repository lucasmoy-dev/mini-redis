package com.miniredis;

import com.miniredis.application.command.CommandDispatcher;
import com.miniredis.application.command.CommandHandler;
import com.miniredis.application.command.impl.*;
import com.miniredis.application.services.RedisService;
import com.miniredis.infrastructure.adapters.out.memory.InMemoryRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class RedisServiceTest {

    private RedisService redisService;

    @BeforeEach
    void setUp() {
        InMemoryRedisRepository repository = new InMemoryRedisRepository();

        List<CommandHandler> handlers = asList(
                new SetCommandHandler(),
                new GetCommandHandler(),
                new DelCommandHandler(),
                new IncrCommandHandler(),
                new DbSizeCommandHandler(),
                new ZAddCommandHandler(),
                new ZCardCommandHandler(),
                new ZRankCommandHandler(),
                new ZRangeCommandHandler());

        CommandDispatcher dispatcher = new CommandDispatcher(handlers);
        redisService = new RedisService(repository, dispatcher);
    }

    @Test
    void set_ShouldReturnOk() {
        assertEquals("OK", redisService.set("key1", "val1"));
    }

    @Test
    void get_ShouldReturnValue_WhenExists() {
        redisService.set("key1", "val1");
        assertEquals("val1", redisService.get("key1"));
    }

    @Test
    void get_ShouldReturnNil_WhenNotExists() {
        assertEquals("(nil)", redisService.get("key1"));
    }

    @Test
    void delete_LibraryShouldReturnCount() {
        redisService.set("key1", "val1");
        assertEquals(1L, redisService.delete("key1"));
        assertEquals(0L, redisService.delete("non_existent"));
    }

    @Test
    void executeCommand_DelShouldReturnOk_AsPerExercise() {
        redisService.set("key1", "val1");
        assertEquals("OK", redisService.executeCommand("DEL key1"));
        assertEquals("OK", redisService.executeCommand("DEL non_existent"));
    }

    @Test
    void incr_ShouldInitializeAndIncrement() {
        assertEquals(1L, redisService.increment("counter"));
        assertEquals(2L, redisService.increment("counter"));
    }

    @Test
    void incr_ShouldThrowError_WhenValueIsNotInteger() {
        redisService.set("not-int", "abc");
        assertThrows(RuntimeException.class, () -> redisService.increment("not-int"));
    }

    @Test
    void setWithExpiry_ShouldExpireKey_AfterTTL() throws InterruptedException {
        redisService.setWithExpiry("key", "val", 1);
        assertEquals("val", redisService.get("key"));
        Thread.sleep(1100);
        assertEquals("(nil)", redisService.get("key"));
    }

    @Test
    void zAdd_ShouldReturnOne_ForNewMember() {
        assertEquals(1L, redisService.zAdd("myset", 1.0, "a"));
    }

    @Test
    void zAdd_ShouldReturnZero_ForExistingMember() {
        redisService.zAdd("myset", 1.0, "a");
        assertEquals(0L, redisService.zAdd("myset", 2.0, "a"));
    }

    @Test
    void zCard_ShouldReturnTotalCount() {
        redisService.zAdd("myset", 1.0, "a");
        redisService.zAdd("myset", 2.0, "b");
        assertEquals(2L, redisService.zCard("myset"));
    }

    @Test
    void zRank_ShouldReturnCorrectPosition() {
        redisService.zAdd("myset", 10.0, "a");
        redisService.zAdd("myset", 20.0, "b");
        assertEquals(0L, redisService.zRank("myset", "a"));
        assertEquals(1L, redisService.zRank("myset", "b"));
    }

    @Test
    void zRank_ShouldReturnNull_WhenMemberNotFound() {
        redisService.zAdd("myset", 10.0, "a");
        assertNull(redisService.zRank("myset", "non_existent"));
    }

    @Test
    void databaseSize_ShouldReturnCorrectCount() {
        redisService.set("k1", "v1");
        redisService.set("k2", "v2");
        assertEquals(2L, redisService.databaseSize());
    }

    @Test
    void zAdd_ShouldReorderElements_WhenScoreIsUpdated() {
        redisService.zAdd("rank", 10.0, "a");
        redisService.zAdd("rank", 20.0, "b");
        assertEquals(asList("a", "b"), redisService.zRange("rank", 0, -1));

        redisService.zAdd("rank", 30.0, "a");

        assertEquals(asList("b", "a"), redisService.zRange("rank", 0, -1));
    }

    @Test
    void zRange_ShouldReturnElementsInOrder() {
        redisService.zAdd("myset", 3.0, "c");
        redisService.zAdd("myset", 1.0, "a");
        redisService.zAdd("myset", 2.0, "b");

        List<String> range = redisService.zRange("myset", 0, -1);
        assertEquals(asList("a", "b", "c"), range);
    }

    @Test
    void executeCommand_SetGet() {
        assertEquals("OK", redisService.executeCommand("SET key val"));
        assertEquals("val", redisService.executeCommand("GET key"));
    }

    @Test
    void executeCommand_UnknownCommand_ShouldReturnError() {
        String result = redisService.executeCommand("NOT_A_COMMAND arg");
        assertTrue(result.startsWith("ERR unknown command"));
    }

    @Test
    void executeCommand_WrongArgs_ShouldReturnError() {
        String result = redisService.executeCommand("SET key");
        assertTrue(result.contains("wrong number of arguments"));
    }

    @Test
    void concurrentIncrements_ShouldBeAccurate() {
        int totalIncrements = 1000;

        IntStream.range(0, totalIncrements)
                .parallel()
                .forEach(i -> redisService.increment("shared_counter"));

        assertEquals(String.valueOf(totalIncrements), redisService.get("shared_counter"));
    }

    @Test
    void concurrentSets_OnSameKey_ShouldWork() {
        int totalSets = 100;

        IntStream.range(0, totalSets)
                .parallel()
                .forEach(i -> redisService.set("key", "val" + i));

        String result = redisService.get("key");
        assertNotEquals("(nil)", result);
        assertTrue(result.startsWith("val"));
    }
}
