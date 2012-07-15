package com.dubious.itunes.statistics.service;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;

/**
 * Services for snapshot objects.
 */
public interface SnapshotService {

    /**
     * Write a snapshot.
     * 
     * @param snapshot The snapshot to write.
     * @throws StatisticsException On error.
     */
    void writeSnapshot(Snapshot snapshot) throws StatisticsException;
}
