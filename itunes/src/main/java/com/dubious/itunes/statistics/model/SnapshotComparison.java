package com.dubious.itunes.statistics.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Result of comparing two snapshots.
 */
public class SnapshotComparison {

    private String firstSnapshot;
    private String secondSnapshot;
    private Collection<SongComparison> songComparisons;

    public SnapshotComparison() {
        songComparisons = new ArrayList<SongComparison>();
    }

    public SnapshotComparison withFirstSnapshot(String firstSnapshot) {
        this.firstSnapshot = firstSnapshot;
        return this;
    }

    public SnapshotComparison withSecondSnapshot(String secondSnapshot) {
        this.secondSnapshot = secondSnapshot;
        return this;
    }

    public SnapshotComparison addSongComparison(SongComparison songComparison) {
        this.songComparisons.add(songComparison);
        return this;
    }

    public SnapshotComparison addSongComparisons(Collection<SongComparison> songComparisons) {
        this.songComparisons.addAll(songComparisons);
        return this;
    }

    public String getFirstSnapshot() {
        return firstSnapshot;
    }

    public String getSecondSnapshot() {
        return secondSnapshot;
    }

    public Collection<SongComparison> getSongComparisons() {
        return Collections.unmodifiableCollection(songComparisons);
    }
}
