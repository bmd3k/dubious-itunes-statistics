package com.dubious.itunes.statistics.model;

import static java.util.Collections.unmodifiableMap;

import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.dubious.itunes.model.Song;

/**
 * A Snapshot of Itunes statistics.
 */
public class Snapshot {

    private String name;
    private DateTime snapshotDate;
    private Map<Song, SongStatistics> statistics;

    public Snapshot() {
        this.statistics = new LinkedHashMap<Song, SongStatistics>();
    }

    public Snapshot withName(String name) {
        this.name = name;
        return this;
    }

    public Snapshot withSnapshotDate(DateTime snapshotDate) {
        this.snapshotDate = snapshotDate;
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

    public DateTime getSnapshotDate() {
        return snapshotDate;
    }

    public Map<Song, SongStatistics> getStatistics() {
        return unmodifiableMap(statistics);
    }
}
