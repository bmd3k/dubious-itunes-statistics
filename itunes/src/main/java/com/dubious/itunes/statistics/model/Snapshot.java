package com.dubious.itunes.statistics.model;

import static java.util.Collections.unmodifiableMap;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.dubious.itunes.model.Song;

/**
 * A Snapshot of Itunes statistics.
 */
public class Snapshot {

    private String name;
    private DateTime date;
    private Map<Song, SongStatistics> statistics;

    /**
     * Constructor.
     */
    public Snapshot() {
        this.statistics = new LinkedHashMap<Song, SongStatistics>();
    }

    /**
     * Set the name of the snapshot.
     * 
     * @param name Name of the snapshot.
     * @return This.
     */
    public final Snapshot withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Set the date of the snapshot.
     * 
     * @param date Date of the snapshot.
     * @return This.
     */
    public final Snapshot withDate(DateTime date) {
        this.date = date;
        return this;
    }

    /**
     * Add statistics for a song to the snapshot.
     * 
     * @param song The song.
     * @param statistic The song statistics.
     * @return This.
     */
    public final Snapshot addStatistic(Song song, SongStatistics statistic) {
        this.statistics.put(song, statistic);
        return this;
    }

    /**
     * Add statistics for songs to the snapshots.
     * 
     * @param statistics The song statistics.
     * @return This.
     */
    public final Snapshot addStatistics(Map<Song, SongStatistics> statistics) {
        this.statistics.putAll(statistics);
        return this;
    }

    /**
     * Get the name of the snapshot.
     * 
     * @return Name of the snapshot.
     */
    public final String getName() {
        return name;
    }

    /**
     * Get the date of the snapshot.
     * 
     * @return Date of the snapshot.
     */
    public final DateTime getDate() {
        return date;
    }

    /**
     * Get the song statistics for the snapshot.
     * 
     * @return Song statistics for the snapshot.
     */
    public final Map<Song, SongStatistics> getStatistics() {
        return unmodifiableMap(statistics);
    }

    @Override
    public final boolean equals(Object refactor) {
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
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
