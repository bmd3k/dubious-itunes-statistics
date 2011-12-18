package com.dubious.itunes.statistics.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.joda.time.DateTime;

/**
 * A Snapshot of Itunes statistics.
 */
public class Snapshot {

    private String name;
    private DateTime snapshotDate;
    private Collection<SongStatistics> statistics;

    public Snapshot() {
        this.statistics = new ArrayList<SongStatistics>();
    }

    public Snapshot withName(String name) {
        this.name = name;
        return this;
    }

    public Snapshot withSnapshotDate(DateTime snapshotDate) {
        this.snapshotDate = snapshotDate;
        return this;
    }

    public Snapshot addStatistic(SongStatistics statistic) {
        this.statistics.add(statistic);
        return this;
    }

    public Snapshot addStatistics(Collection<SongStatistics> statistics) {
        this.statistics.addAll(statistics);
        return this;
    }

    public String getName() {
        return name;
    }

    public DateTime getSnapshotDate() {
        return snapshotDate;
    }

    public Collection<SongStatistics> getStatistics() {
        return Collections.unmodifiableCollection(statistics);
    }
}
