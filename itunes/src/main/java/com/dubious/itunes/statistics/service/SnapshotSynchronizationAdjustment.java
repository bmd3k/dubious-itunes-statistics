package com.dubious.itunes.statistics.service;

import com.dubious.itunes.statistics.model.Snapshot;

/**
 * Describes an object that adjusts snapshots during synchronization between repositories.
 */
public interface SnapshotSynchronizationAdjustment {

    /**
     * Adjust a snapshot from the source repository before synchronizing it to the target
     * repository.
     * 
     * @param snapshot The snapshot to adjust.
     * @return The adjusted copy of the snapshot.
     */
    Snapshot adjustSnapshotForSynchronization(Snapshot snapshot);
}
