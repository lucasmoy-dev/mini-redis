package com.miniredis.application.services;

import static com.miniredis.domain.constant.RedisConstants.*;
import com.miniredis.application.command.CommandDispatcher;
import com.miniredis.domain.exception.RedisException;
import com.miniredis.domain.ports.in.RedisPort;
import com.miniredis.domain.ports.out.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Slf4j
@Service("redisService")
public class RedisService implements RedisPort {

    private final RedisRepository repository;
    private final CommandDispatcher dispatcher;
    private final Pattern ALLOWED_PATTERN = Pattern.compile("^[a-zA-Z0-9-_:]+$");

    public RedisService(RedisRepository repository, CommandDispatcher dispatcher) {
        this.repository = repository;
        this.dispatcher = dispatcher;
    }

    private void validate(String... values) {
        for (String val : values) {
            if (val != null && !ALLOWED_PATTERN.matcher(val).matches()) {
                throw RedisException.invalidCharacters();
            }
        }
    }

    @Override
    public String set(String key, String value) {
        validate(key, value);
        repository.set(key, value, null);
        return OK;
    }

    @Override
    public String setWithExpiry(String key, String value, long seconds) {
        validate(key, value);
        long expiryTime = System.currentTimeMillis() + (seconds * 1000);
        repository.set(key, value, expiryTime);
        return OK;
    }

    @Override
    public String get(String key) {
        validate(key);
        return repository.get(key).orElse(NIL);
    }

    @Override
    public Long delete(String key) {
        validate(key);
        return repository.delete(key) ? VALUE_EXISTS : VALUE_NOT_EXISTS;
    }

    @Override
    public Long databaseSize() {
        return repository.databaseSize();
    }

    @Override
    public void flushAll() {
        repository.flushAll();
    }

    @Override
    public Long increment(String key) {
        validate(key);
        return repository.increment(key);
    }

    @Override
    public Long zAdd(String key, double score, String member) {
        validate(key, member);
        return repository.zAdd(key, score, member);
    }

    @Override
    public Long zCard(String key) {
        validate(key);
        return repository.zCard(key);
    }

    @Override
    public Long zRank(String key, String member) {
        validate(key, member);
        return repository.zRank(key, member).orElse(null);
    }

    @Override
    public List<String> zRange(String key, int start, int stop) {
        validate(key);
        return repository.zRange(key, start, stop);
    }

    @Override
    public String executeCommand(String commandLine) {
        return dispatcher.execute(this, commandLine);
    }

    public void cleanup() {
        repository.cleanupExpired();
    }
}
