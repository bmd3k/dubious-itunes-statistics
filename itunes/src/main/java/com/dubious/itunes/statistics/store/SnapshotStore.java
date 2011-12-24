package com.dubious.itunes.statistics.store;

import com.dubious.itunes.statistics.model.Snapshot;

/**
 * Describes read/write storage of snapshots.
 */
public interface SnapshotStore extends ReadOnlySnapshotStore {

    /**
     * Write a snapshot to the store.
     * 
     * @param snapshot The snapshot information to write.
     * @throws StoreException On error.
     */
    void writeSnapshot(Snapshot snapshot) throws StoreException;

    /**
     * Delete all snapshots in the store.
     * 
     * @throws StoreException On error.
     */
    void deleteAll() throws StoreException;
}
