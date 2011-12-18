package com.dubious.itunes.statistics.model;

/**
 * Result of comparing a song from two different snapshots.
 */
public class SongComparison extends SongKey<SongComparison> {

    private Integer firstPlayCount;
    private Integer secondPlayCount;

    public SongComparison withFirstPlayCount(Integer firstPlayCount) {
        this.firstPlayCount = firstPlayCount;
        return this;
    }

    public SongComparison withSecondPlayCount(Integer secondPlayCount) {
        this.secondPlayCount = secondPlayCount;
        return this;
    }

    public Integer getFirstPlayCount() {
        return firstPlayCount;
    }

    public Integer getSecondPlayCount() {
        return secondPlayCount;
    }

    @Override
    public SongComparison getThis() {
        return this;
    }
}
