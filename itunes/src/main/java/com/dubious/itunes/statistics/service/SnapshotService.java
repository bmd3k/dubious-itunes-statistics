package com.dubious.itunes.statistics.service;

import com.dubious.itunes.statistics.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;

/**
 * Provides historical information using statistical snapshots.
 */
public interface SnapshotService {

    /**
     * Provide a simple "history" consisting of two snapshots.
     * 
     * @param snapshot1 A snapshot to use for the comparison.
     * @param snapshot2 The other snapshot to use for the comparison.
     * @return Comparison of the snapshots.
     * @throws StatisticsException On error.
     */
    SnapshotsHistory compareSnapshots(String snapshot1, String snapshot2)
            throws StatisticsException;
}
