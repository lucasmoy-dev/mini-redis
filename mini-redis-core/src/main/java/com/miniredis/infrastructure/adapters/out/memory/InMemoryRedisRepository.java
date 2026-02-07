package com.miniredis.infrastructure.adapters.out.memory;

import com.miniredis.domain.exception.RedisException;
import com.miniredis.domain.model.RedisSortedSet;
import com.miniredis.domain.ports.out.RedisRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.miniredis.domain.constant.RedisConstants.*;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class InMemoryRedisRepository implements RedisRepository {

    private final Map<String, Object> store = new ConcurrentHashMap<>();
    private final Map<String, Long> expiries = new ConcurrentHashMap<>();

    @Override
    public void set(String key, String value, Long expiryTime) {
        String keyLock = key.intern();
        synchronized (keyLock) {
            store.put(key, value);
            if (nonNull(expiryTime)) {
                expiries.put(key, expiryTime);
            } else {
                expiries.remove(key);
            }
        }
    }

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(fetchAs(key, String.class));
    }

    @Override
    public boolean delete(String key) {
        String keyLock = key.intern();
        synchronized (keyLock) {
            return removeInternal(key);
        }
    }

    @Override
    public long increment(String key) {
        String keyLock = key.intern();
        synchronized (keyLock) {
            String val = fetchAs(key, String.class);
            long newValue;

            if (isNull(val)) {
                newValue = DEFAULT_INCREMENT_START;
            } else {
                try {
                    newValue = Long.parseLong(val) + 1;
                } catch (NumberFormatException e) {
                    throw RedisException.notInteger();
                }
            }

            store.put(key, String.valueOf(newValue));
            return newValue;
        }
    }

    @Override
    public long databaseSize() {
        cleanupExpired();
        return (long) store.size();
    }

    @Override
    public void flushAll() {
        store.clear();
        expiries.clear();
    }

    @Override
    public long zAdd(String key, double score, String member) {
        String keyLock = key.intern();
        synchronized (keyLock) {
            RedisSortedSet zset = fetchAs(key, RedisSortedSet.class);
            if (isNull(zset)) {
                zset = new RedisSortedSet();
                store.put(key, zset);
            }
            return zset.add(member, score);
        }
    }

    @Override
    public long zCard(String key) {
        String keyLock = key.intern();
        synchronized (keyLock) {
            RedisSortedSet zset = fetchAs(key, RedisSortedSet.class);
            return isNull(zset) ? 0L : zset.size();
        }
    }

    @Override
    public Optional<Long> zRank(String key, String member) {
        String keyLock = key.intern();
        synchronized (keyLock) {
            RedisSortedSet zset = fetchAs(key, RedisSortedSet.class);
            return isNull(zset) ? Optional.empty() : zset.rank(member);
        }
    }

    @Override
    public List<String> zRange(String key, int start, int stop) {
        String keyLock = key.intern();
        synchronized (keyLock) {
            RedisSortedSet zset = fetchAs(key, RedisSortedSet.class);
            return isNull(zset) ? emptyList() : zset.range(start, stop);
        }
    }

    @Override
    public void cleanupExpired() {
        long now = System.currentTimeMillis();
        expiries.entrySet().removeIf(entry -> {
            if (now > entry.getValue()) {
                store.remove(entry.getKey());
                return true;
            }
            return false;
        });
    }

    private <T> T fetchAs(String key, Class<T> type) {
        checkExpiration(key);
        Object val = store.get(key);
        if (nonNull(val) && !type.isInstance(val)) {
            throw RedisException.wrongType();
        }
        return type.cast(val);
    }

    private void checkExpiration(String key) {
        long now = System.currentTimeMillis();
        if (isExpired(key, now)) {
            String keyLock = key.intern();
            synchronized (keyLock) {
                if (isExpired(key, now)) {
                    removeInternal(key);
                }
            }
        }
    }

    private boolean removeInternal(String key) {
        expiries.remove(key);
        return store.remove(key) != null;
    }

    private boolean isExpired(String key, long now) {
        Long expiry = expiries.get(key);
        return nonNull(expiry) && now > expiry;
    }
}
