package com.dubious.itunes.statistics.service;

import java.util.List;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;

/**
 * Provides historical information using statistical snapshots.
 */
public interface HistoryService {

    /**
     * Provide a simple "history" consisting of two snapshots.
     * 
     * @param snapshots Snapshots to use for the comparison.
     * @return Comparison of the snapshots.
     * @throws StatisticsException On error.
     */
    SnapshotsHistory compareSnapshots(List<String> snapshots) throws StatisticsException;
}
