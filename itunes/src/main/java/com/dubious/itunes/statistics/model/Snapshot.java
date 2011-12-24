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

    public Snapshot() {
        this.statistics = new LinkedHashMap<Song, SongStatistics>();
    }

    public Snapshot withName(String name) {
        this.name = name;
        return this;
    }

    public Snapshot withDate(DateTime date) {
        this.date = date;
        return this;
    }

    public Snapshot addStatistic(Song song, SongStatistics statistic) {
        this.statistics.put(song, statistic);
        return this;
    }

    public Snapshot addStatistics(Map<Song, SongStatistics> statistics) {
        this.statistics.putAll(statistics);
        return this;
    }

    public String getName() {
        return name;
    }

    public DateTime getDate() {
        return date;
    }

    public Map<Song, SongStatistics> getStatistics() {
        return unmodifiableMap(statistics);
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
