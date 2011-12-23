package com.dubious.itunes.statistics.model;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Statistical history using snapshots.
 */
public class SnapshotsHistory {

    private String firstSnapshot;
    private String secondSnapshot;
    private List<SongHistory> songHistories;

    public SnapshotsHistory() {
        songHistories = new ArrayList<SongHistory>();
    }

    public SnapshotsHistory withEarliestSnapshot(String firstSnapshot) {
        this.firstSnapshot = firstSnapshot;
        return this;
    }

    public SnapshotsHistory withLatestSnapshot(String secondSnapshot) {
        this.secondSnapshot = secondSnapshot;
        return this;
    }

    public SnapshotsHistory addSongHistory(SongHistory songHistory) {
        this.songHistories.add(songHistory);
        return this;
    }

    public SnapshotsHistory addSongHistories(Collection<SongHistory> songHistories) {
        this.songHistories.addAll(songHistories);
        return this;
    }

    public String getFirstSnapshot() {
        return firstSnapshot;
    }

    public String getSecondSnapshot() {
        return secondSnapshot;
    }

    public List<SongHistory> getSongHistories() {
        return unmodifiableList(songHistories);
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
