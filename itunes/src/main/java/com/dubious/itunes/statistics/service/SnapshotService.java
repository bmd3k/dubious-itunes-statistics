package com.dubious.itunes.statistics.service;

import com.dubious.itunes.model.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;

/**
 * Provides historical information using statistical snapshots.
 */
public interface SnapshotService {

    /**
     * Provide a simple "history" consisting of two snapshots.
     */
    SnapshotsHistory compareSnapshots(String snapshot1, String snapshot2)
            throws StatisticsException;
}
