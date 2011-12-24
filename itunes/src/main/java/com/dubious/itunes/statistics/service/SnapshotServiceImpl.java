package com.dubious.itunes.statistics.service;

import java.util.Map;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.store.ReadOnlySnapshotStore;

/**
 * Implementation of service for providing historical information using statistical snapshots.
 */
public class SnapshotServiceImpl implements SnapshotService {

    private ReadOnlySnapshotStore snapshotStore;

    /**
     * Constructor.
     * 
     * @param snapshotStore {@link ReadOnlySnapshotStore} to inject.
     */
    public SnapshotServiceImpl(ReadOnlySnapshotStore snapshotStore) {
        this.snapshotStore = snapshotStore;
    }

    @Override
    public final SnapshotsHistory compareSnapshots(String snapshotName1, String snapshotName2)
            throws StatisticsException {

        Snapshot snapshot1 = getSnapshotIfExists(snapshotName1);
        Snapshot snapshot2 = getSnapshotIfExists(snapshotName2);

        // choose the "latest" snapshot as the root of the search since we assume it has the
        // superset of songs in which this is interested. the "earliest" snapshot may have some
        // songs that the "latest" does not but these tend to be songs that have been deleted from
        // the system.
        Snapshot earliestSnapshot = snapshot1;
        Snapshot latestSnapshot = snapshot2;
        if (snapshot2.getDate().isBefore(snapshot1.getDate())) {
            earliestSnapshot = snapshot2;
            latestSnapshot = snapshot1;
        }

        SnapshotsHistory history =
                new SnapshotsHistory()
                        .withEarliestSnapshot(earliestSnapshot.getName())
                        .withLatestSnapshot(latestSnapshot.getName());

        for (Map.Entry<Song, SongStatistics> latestStatistic : latestSnapshot
                .getStatistics()
                .entrySet()) {
            Song song = latestStatistic.getKey();
            SongHistory songHistory =
                    new SongHistory()
                            .withArtistName(song.getArtistName())
                            .withAlbumName(song.getAlbumName())
                            .withSongName(song.getName())
                            .withLatestPlayCount(latestStatistic.getValue().getPlayCount());

            SongStatistics earliestStatistic = earliestSnapshot.getStatistics().get(song);
            if (earliestStatistic != null) {
                songHistory.withEarliestPlayCount(earliestStatistic.getPlayCount());
            }

            history.addSongHistory(songHistory);
        }

        return history;
    }

    /**
     * Retrieve a snapshot from the store if it exists.
     * 
     * @param snapshotName The name of the snapshot.
     * @return The snapshot if it exists.
     * @throws StatisticsException If the snapshot could not be found.
     */
    private Snapshot getSnapshotIfExists(String snapshotName) throws StatisticsException {
        Snapshot snapshot = snapshotStore.getSnapshot(snapshotName);
        if (snapshot == null) {
            throw new StatisticsException("Could not find snapshot with name [" + snapshotName
                    + "]");
        }
        return snapshot;
    }
}
