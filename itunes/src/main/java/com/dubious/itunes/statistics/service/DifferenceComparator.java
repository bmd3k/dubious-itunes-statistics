package com.dubious.itunes.statistics.service;

import java.util.Comparator;

import com.dubious.itunes.statistics.model.SongHistory;

/**
 * Compare SongHistories by comparing difference in play counts between two snapshots.
 */
public class DifferenceComparator implements Comparator<SongHistory> {
    private String earliestSnapshot;
    private String latestSnapshot;

    /**
     * Constructor.
     * 
     * @param earliestSnapshot Earliest snapshot in the comparisons.
     * @param latestSnapshot Latest snapshot in the comparisons.
     */
    public DifferenceComparator(String earliestSnapshot, String latestSnapshot) {
        this.earliestSnapshot = earliestSnapshot;
        this.latestSnapshot = latestSnapshot;
    }

    @Override
    public final int compare(SongHistory songHistory1, SongHistory songHistory2) {
        int difference1 =
                songHistory1.getSongStatistics().get(latestSnapshot).getPlayCount()
                        - songHistory1.getSongStatistics().get(earliestSnapshot).getPlayCount();
        int difference2 =
                songHistory2.getSongStatistics().get(latestSnapshot).getPlayCount()
                        - songHistory2.getSongStatistics().get(earliestSnapshot).getPlayCount();
        return difference2 - difference1;
    }
}
