package com.dubious.itunes.statistics.service;

import com.dubious.itunes.statistics.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;

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
}
