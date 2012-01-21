package com.dubious.itunes.statistics.service;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;

/**
 * Operations for writing analysis to file.
 */
public interface FileOutputService {

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
     * Write history to file. Elements are sorted based on latest play count, descending.
     * 
     * @param history The history to use for the analysis.
     * @param outputPath The output path.
     * @param order The order.
     * @throws StatisticsException On error.
     */
    void writeSnapshotsHistory(SnapshotsHistory history, String outputPath, Order order)
            throws StatisticsException;

}
