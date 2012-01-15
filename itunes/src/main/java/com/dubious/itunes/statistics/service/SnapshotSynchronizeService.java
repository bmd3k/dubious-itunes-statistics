package com.dubious.itunes.statistics.service;

import com.dubious.itunes.statistics.exception.StatisticsException;

/**
 * Synchronize snapshots from one store to another.
 */
public interface SnapshotSynchronizeService {

    /**
     * Update snapshots from one store to another.
     * 
     * @throws StatisticsException On error.
     */
    void synchronizeSnapshots() throws StatisticsException;
}
