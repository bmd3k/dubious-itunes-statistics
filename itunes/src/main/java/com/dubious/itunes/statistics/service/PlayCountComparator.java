package com.dubious.itunes.statistics.service;

import java.util.Comparator;

import com.dubious.itunes.statistics.model.SongHistory;

/**
 * Compare SongHistory by comparing play counts from a snapshot.
 */
public class PlayCountComparator implements Comparator<SongHistory> {
    private String snapshot;

    /**
     * Constructor.
     * 
     * @param snapshot The snapshot to use for the comparison.
     */
    public PlayCountComparator(String snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    public final int compare(SongHistory songHistory1, SongHistory songHistory2) {
        return songHistory2.getSongStatistics().get(snapshot).getPlayCount()
                - songHistory1.getSongStatistics().get(snapshot).getPlayCount();
    }
}
