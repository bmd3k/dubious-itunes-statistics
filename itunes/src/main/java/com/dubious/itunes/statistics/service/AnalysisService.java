package com.dubious.itunes.statistics.service;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;

/**
 * Snapshot-based statistical analysis.
 */
public interface AnalysisService {

    /**
     * Write analysis to file. Elements are sorted based on latest play count, descending.
     * 
     * @param history The history to use for the analysis.
     * @param outputPath The output path.
     * @throws StatisticsException On error.
     */
    void writeAnalysis(SnapshotsHistory history, String outputPath) throws StatisticsException;

    /**
     * Write analysis to file, sorting elements by difference in play counts, descending.
     * 
     * @param history The history to use for the analysis.
     * @param outputPath The output path.
     * @throws StatisticsException On error.
     */
    void writeAnalysisOrderByDifference(SnapshotsHistory history, String outputPath)
            throws StatisticsException;

    /**
     * Enrich a song history with other analysis.
     * 
     * @param songHistory The history to analyze.
     * @return Enriched version of songHistory.
     */
    SongHistory enrichSongHistory(SongHistory songHistory);
}
