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

    private List<String> snapshots;
    private List<SongHistory> songHistories;

    /**
     * Constructor.
     */
    public SnapshotsHistory() {
        snapshots = new ArrayList<String>();
        songHistories = new ArrayList<SongHistory>();
    }

    /**
     * Add the name of a snapshot represented in this history.
     * 
     * @param snapshot The name of a snapshot represented in this history.
     * @return This.
     */
    public final SnapshotsHistory addSnapshot(String snapshot) {
        this.snapshots.add(snapshot);
        return this;
    }

    /**
     * Add a group of names of snapshots represented in this history.
     * 
     * @param snapshots The names of snapshots represented in this history.
     * @return This.
     */
    public final SnapshotsHistory addSnapshots(List<String> snapshots) {
        this.snapshots.addAll(snapshots);
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
     * Retrieve the names of snapshots represented in this history.
     * 
     * @return The names of snapshots represented in this history.
     */
    public final List<String> getSnapshots() {
        return unmodifiableList(snapshots);
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
