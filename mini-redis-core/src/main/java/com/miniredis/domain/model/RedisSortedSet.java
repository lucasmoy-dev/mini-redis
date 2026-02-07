package com.miniredis.domain.model;

import java.util.*;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static com.miniredis.domain.constant.RedisConstants.VALUE_EXISTS;
import static com.miniredis.domain.constant.RedisConstants.VALUE_NOT_EXISTS;

public class RedisSortedSet {
    private final Map<String, Double> memberToScore = new HashMap<>();
    private final TreeSet<SortedSetMember> scoreToMember = new TreeSet<>();

    public long add(String member, double score) {
        Double oldScore = memberToScore.put(member, score);
        if (nonNull(oldScore)) {
            scoreToMember.remove(new SortedSetMember(member, oldScore));
        }
        scoreToMember.add(new SortedSetMember(member, score));
        return isNull(oldScore) ? VALUE_EXISTS : VALUE_NOT_EXISTS;
    }

    public long size() {
        return memberToScore.size();
    }

    public Optional<Long> rank(String member) {
        List<String> all = scoreToMember.stream()
                .map(SortedSetMember::getMember)
                .collect(toList());

        int index = all.indexOf(member);
        return index >= 0 ? Optional.of((long) index) : Optional.empty();
    }

    public List<String> range(int start, int stop) {
        List<String> allMembers = scoreToMember.stream()
                .map(SortedSetMember::getMember)
                .collect(toList());

        int size = allMembers.size();
        if (size == 0) {
            return emptyList();
        }

        int normalizedStart = normalizeIndex(start, size);
        int normalizedStop = normalizeIndex(stop, size);

        if (normalizedStart >= size || normalizedStart > normalizedStop) {
            return emptyList();
        }
        int finalStop = Math.min(size - 1, normalizedStop);
        return new ArrayList<>(allMembers.subList(normalizedStart, finalStop + 1));
    }

    private int normalizeIndex(int index, int size) {
        if (index < 0) {
            index = size + index;
        }
        return Math.max(0, index);
    }
}
