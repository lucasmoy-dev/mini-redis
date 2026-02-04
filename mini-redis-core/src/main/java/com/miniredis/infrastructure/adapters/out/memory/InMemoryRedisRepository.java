package com.miniredis.infrastructure.adapters.out.memory;

import com.miniredis.domain.constant.RedisConstants;
import com.miniredis.domain.exception.RedisException;
import com.miniredis.domain.model.SortedSetMember;
import com.miniredis.domain.ports.out.RedisRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryRedisRepository implements RedisRepository {

    private final Map<String, Object> store = new ConcurrentHashMap<>();
    private final Map<String, Long> expirations = new ConcurrentHashMap<>();

    private static class SortedSet {
        final Map<String, Double> memberToScore = new HashMap<>();
        final TreeSet<SortedSetMember> scoreToMember = new TreeSet<>();

        SortedSet() {
        }
    }

    @Override
    public void set(String key, String value, Long expiryTimeMillis) {
        store.put(key, value);
        if (expiryTimeMillis != null) {
            expirations.put(key, expiryTimeMillis);
        } else {
            expirations.remove(key);
        }
    }

    @Override
    public Optional<String> get(String key) {
        if (isExpired(key)) {
            delete(key);
            return Optional.empty();
        }
        Object val = store.get(key);
        if (val instanceof String) {
            return Optional.of((String) val);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(String key) {
        expirations.remove(key);
        return store.remove(key) != null;
    }

    @Override
    public long dbSize() {
        cleanupExpired();
        return store.size();
    }

    @Override
    public long increment(String key) {
        synchronized (key.intern()) {
            if (isExpired(key)) {
                delete(key);
            }
            Object val = store.get(key);
            long newValue;
            if (val == null) {
                newValue = 1;
            } else if (val instanceof String) {
                try {
                    newValue = Long.parseLong((String) val) + 1;
                } catch (NumberFormatException e) {
                    throw new RedisException(RedisConstants.ERR_NOT_INTEGER);
                }
            } else {
                throw new RedisException(RedisConstants.ERR_WRONG_TYPE);
            }
            store.put(key, String.valueOf(newValue));
            return newValue;
        }
    }

    @Override
    public long zAdd(String key, double score, String member) {
        synchronized (key.intern()) {
            if (isExpired(key)) {
                delete(key);
            }
            Object val = store.get(key);
            SortedSet zset;
            if (val == null) {
                zset = new SortedSet();
                store.put(key, zset);
            } else if (val instanceof SortedSet) {
                zset = (SortedSet) val;
            } else {
                throw new RedisException(RedisConstants.ERR_WRONG_TYPE);
            }

            Double oldScore = zset.memberToScore.put(member, score);
            if (oldScore != null) {
                zset.scoreToMember.remove(new SortedSetMember(member, oldScore));
            }
            zset.scoreToMember.add(new SortedSetMember(member, score));

            return oldScore == null ? 1 : 0;
        }
    }

    @Override
    public long zCard(String key) {
        if (isExpired(key)) {
            delete(key);
            return 0;
        }
        Object val = store.get(key);
        if (val instanceof SortedSet) {
            return ((SortedSet) val).memberToScore.size();
        }
        return 0;
    }

    @Override
    public Optional<Long> zRank(String key, String member) {
        if (isExpired(key)) {
            delete(key);
            return Optional.empty();
        }
        Object val = store.get(key);
        if (val instanceof SortedSet) {
            SortedSet zset = (SortedSet) val;
            Double score = zset.memberToScore.get(member);
            if (score == null)
                return Optional.empty();

            long rank = 0;
            for (SortedSetMember m : zset.scoreToMember) {
                if (m.getMember().equals(member))
                    return Optional.of(rank);
                rank++;
            }
        }
        return Optional.empty();
    }

    @Override
    public List<String> zRange(String key, int start, int stop) {
        if (isExpired(key)) {
            delete(key);
            return Collections.emptyList();
        }
        Object val = store.get(key);
        if (val instanceof SortedSet) {
            SortedSet zset = (SortedSet) val;
            List<String> allMembers = zset.scoreToMember.stream()
                    .map(SortedSetMember::getMember)
                    .collect(Collectors.toList());

            int size = allMembers.size();
            if (size == 0)
                return Collections.emptyList();

            // Handle negative indices
            if (start < 0)
                start = size + start;
            if (stop < 0)
                stop = size + stop;

            if (start < 0)
                start = 0;
            if (start >= size)
                return Collections.emptyList();
            if (stop >= size)
                stop = size - 1;
            if (start > stop)
                return Collections.emptyList();

            return allMembers.subList(start, stop + 1);
        }
        return Collections.emptyList();
    }

    @Override
    public void cleanupExpired() {
        long now = System.currentTimeMillis();
        expirations.forEach((key, expiry) -> {
            if (now >= expiry) {
                delete(key);
            }
        });
    }

    private boolean isExpired(String key) {
        Long expiry = expirations.get(key);
        return expiry != null && System.currentTimeMillis() >= expiry;
    }
}
