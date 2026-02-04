package com.miniredis.domain.model;

import java.util.Objects;

public class SortedSetMember implements Comparable<SortedSetMember> {
    private String member;
    private double score;

    public SortedSetMember() {
    }

    public SortedSetMember(String member, double score) {
        this.member = member;
        this.score = score;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int compareTo(SortedSetMember other) {
        if (this.score != other.score) {
            return Double.compare(this.score, other.score);
        }
        return this.member.compareTo(other.member);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SortedSetMember that = (SortedSetMember) o;
        return Objects.equals(member, that.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member);
    }
}
