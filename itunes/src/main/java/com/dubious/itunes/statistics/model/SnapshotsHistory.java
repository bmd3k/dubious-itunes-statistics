package com.dubious.itunes.statistics.model;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Statistical history using snapshots.
 */
public class SnapshotsHistory {

    private String earliestSnapshot;
    private String latestSnapshot;
    private List<SongHistory> songHistories;

    /**
     * Constructor.
     */
    public SnapshotsHistory() {
        songHistories = new ArrayList<SongHistory>();
    }

    /**
     * Set the earliest of the snapshots.
     * 
     * @param earliestSnapshot The earliest snapshot.
     * @return This.
     */
    public final SnapshotsHistory withEarliestSnapshot(String earliestSnapshot) {
        this.earliestSnapshot = earliestSnapshot;
        return this;
    }

    /**
     * Set the latest of the snapshots.
     * 
     * @param latestSnapshot The latest snapshot.
     * @return This.
     */
    public final SnapshotsHistory withLatestSnapshot(String latestSnapshot) {
        this.latestSnapshot = latestSnapshot;
        return this;
    }

    /**
     * Add history of a song related to the snapshots.
     * 
     * @param songHistory History of a song.
     * @return This.
     */
    public final SnapshotsHistory addSongHistory(SongHistory songHistory) {
        this.songHistories.add(songHistory);
        return this;
    }

    /**
     * Add one or more histories of songs related to the snapshots.
     * 
     * @param songHistories Histories of songs.
     * @return This.
     */
    public final SnapshotsHistory addSongHistories(List<SongHistory> songHistories) {
        this.songHistories.addAll(songHistories);
        return this;
    }

    /**
     * Retrieve the earliest of the snapshots.
     * 
     * @return The earliest of the snapshots.
     */
    public final String getEarliestSnapshot() {
        return earliestSnapshot;
    }

    /**
     * Retrieve the latest of the snapshots.
     * 
     * @return The latest of the snapshots.
     */
    public final String getLatestSnapshot() {
        return latestSnapshot;
    }

    /**
     * Retrieve the histories of songs related to the snapshots.
     * 
     * @return The histories of songs.
     */
    public final List<SongHistory> getSongHistories() {
        return unmodifiableList(songHistories);
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
