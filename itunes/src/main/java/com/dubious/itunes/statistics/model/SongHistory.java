package com.dubious.itunes.statistics.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Statistical history of a song.
 */
public class SongHistory extends SongKey<SongHistory> {

    private Map<String, SongStatistics> songStatistics;

    /**
     * Constructor.
     */
    public SongHistory() {
        this.songStatistics = new LinkedHashMap<String, SongStatistics>();
    }

    @Override
    public final SongHistory getThis() {
        return this;
    }

    /**
     * Add statistics for the song from a snapshot.
     * 
     * @param snapshotName The name of the snapshot.
     * @param songStatistics The statistics of the song from the snapshot.
     * @return This.
     */
    public final SongHistory addSongStatistics(String snapshotName, SongStatistics songStatistics) {
        this.songStatistics.put(snapshotName, songStatistics);
        return this;
    }

    /**
     * Get the map of snapshot to statistics.
     * 
     * @return The map of snapshot to statistics.
     */
    public final Map<String, SongStatistics> getSongStatistics() {
        return Collections.unmodifiableMap(songStatistics);
    }

    @Override
    public final boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
