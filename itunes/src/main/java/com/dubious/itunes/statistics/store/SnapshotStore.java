package com.dubious.itunes.statistics.store;

import java.util.List;
import java.util.Map;

import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;

/**
 * Describes read/write storage of snapshots.
 */
public interface SnapshotStore {

    /**
     * Get a snapshot.
     * 
     * @param snapshotName The name of the snapshot.
     * @return The snapshot.
     * @throws StoreException On error.
     */
    Snapshot getSnapshot(String snapshotName) throws StoreException;

    /**
     * Get all snapshots in the store.
     * 
     * @return All snapshots.
     * @throws StoreException On error.
     */
    List<Snapshot> getSnapshots() throws StoreException;

    /**
     * Retrieve a list of snapshots, omitting statistics.
     * 
     * @param snapshotNames The names of the snapshots to retrieve.
     * @return Map of snapshot name to snapshot.
     * @throws StoreException On error.
     */
    Map<String, Snapshot> getSnapshotsWithoutStatistics(List<String> snapshotNames)
            throws StoreException;

    /**
     * Retrieve all snapshots in the store, omitting statistics.
     * 
     * @return All snapshots.
     * @throws StoreException On error.
     */
    List<Snapshot> getSnapshotsWithoutStatistics() throws StoreException;

    /**
     * Retrieve the statistics for a song from a list of snapshots.
     * 
     * @param artistName The artist name.
     * @param albumName The album name.
     * @param songName The song name.
     * @param snapshotNames The snapshot names.
     * @return Map of snapshot name to song statistics for that snapshot.
     * @throws StoreException On error.
     */
    Map<String, SongStatistics> getSongStatisticsFromSnapshots(
            String artistName,
            String albumName,
            String songName,
            List<String> snapshotNames) throws StoreException;

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
