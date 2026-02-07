package com.miniredis.domain.model;

import lombok.*;

@Value
public class SortedSetMember implements Comparable<SortedSetMember> {
    String member;
    double score;

    @Override
    public int compareTo(SortedSetMember other) {
        int scoreComparison = Double.compare(this.score, other.score);
        if (scoreComparison != 0) {
            return scoreComparison;
        }
        return this.member.compareTo(other.member);
    }
}
