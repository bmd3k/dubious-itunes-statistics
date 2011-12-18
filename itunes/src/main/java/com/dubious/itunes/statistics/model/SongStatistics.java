package com.dubious.itunes.statistics.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Statistics for a song for a specific snapshot.
 */
public class SongStatistics extends SongKey<SongStatistics> {
    private String snapshotName;
    Integer playCount;

    public SongStatistics withSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
        return this;
    }

    public SongStatistics withPlayCount(Integer playCount) {
        this.playCount = playCount;
        return this;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    @Override
    public SongStatistics getThis() {
        return this;
    }

    @Override
    public boolean equals(Object refactor) {
        if (refactor == null) {
            return false;
        }
        if (refactor == this) {
            return true;
        }
        if (refactor.getClass() != getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, refactor);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
