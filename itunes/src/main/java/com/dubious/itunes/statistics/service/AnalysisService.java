package com.dubious.itunes.statistics.service;

import java.util.List;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;

/**
 * Enrich history with more analytical information.
 */
public interface AnalysisService {

    /**
     * Defines order of write.
     */
    public enum Order {
        /**
         * Order by play count.
         */
        PlayCount,
        /**
         * Order by the difference in play counts during the history period.
         */
        Difference;
    }

    /**
     * Get an enriched version of snapshots history.
     * 
     * @param snapshots The snapshots to use in the history.
     * @param order How to order the song histories.
     * @return The snapshots history.
     * @throws StatisticsException On error.
     */
    SnapshotsHistory getEnrichedSnapshotsHistory(List<String> snapshots, Order order)
            throws StatisticsException;
}
