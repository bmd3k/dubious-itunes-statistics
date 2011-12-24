package com.dubious.itunes.statistics.store;

import com.dubious.itunes.statistics.model.Snapshot;

/**
 * Describes read-only storage of snapshots.
 */
public interface ReadOnlySnapshotStore {

    /**
     * Get a snapshot.
     * 
     * @param snapshotName The name of the snapshot.
     * @return The snapshot.
     * @throws StoreException On error.
     */
    Snapshot getSnapshot(String snapshotName) throws StoreException;
}
