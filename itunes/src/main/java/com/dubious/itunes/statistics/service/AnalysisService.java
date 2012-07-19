package com.dubious.itunes.statistics.service;

import java.util.List;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;

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
     * @return Snapshots history.
     * @throws StatisticsException On error.
     */
    SnapshotsHistory getEnrichedSnapshotsHistory(List<String> snapshots, Order order)
            throws StatisticsException;

    /**
     * Get an enriched version of song history.
     * 
     * @param artistName Artist name.
     * @param albumName Album name.
     * @param songName Song name.
     * @param snapshots Snapshots to include in the history.
     * @return Song history.
     * @throws StatisticsException On error.
     */
    SongHistory getEnrichedSongHistory(
            String artistName,
            String albumName,
            String songName,
            List<String> snapshots) throws StatisticsException;
}
