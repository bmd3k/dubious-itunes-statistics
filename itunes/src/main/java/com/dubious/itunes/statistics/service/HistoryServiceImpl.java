package com.dubious.itunes.statistics.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.InsufficientSnapshotsSpecifiedException;
import com.dubious.itunes.statistics.exception.SnapshotDoesNotExistException;
import com.dubious.itunes.statistics.exception.SongDoesNotExistForSnapshotsException;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.store.SnapshotStore;

/**
 * Implementation of service for providing historical information using statistical snapshots.
 */
public class HistoryServiceImpl implements HistoryService {

    private static final Integer MIN_SNAPSHOTS_FOR_HISTORY = 2;

    private SnapshotStore snapshotStore;
    private SnapshotFilter snapshotFilter;

    /**
     * Constructor.
     * 
     * @param snapshotStore {@link SnapshotStore} to inject.
     * @param snapshotFilter {@link SnapshotFilter} to inject.
     */
    public HistoryServiceImpl(SnapshotStore snapshotStore, SnapshotFilter snapshotFilter) {
        this.snapshotStore = snapshotStore;
        this.snapshotFilter = snapshotFilter;
    }

    @Override
    public final SnapshotsHistory generateSnapshotHistory(List<String> snapshotNames)
            throws StatisticsException {

        if (snapshotNames.size() < MIN_SNAPSHOTS_FOR_HISTORY) {
            throw new InsufficientSnapshotsSpecifiedException(
                    MIN_SNAPSHOTS_FOR_HISTORY,
                    snapshotNames);
        }

        List<Snapshot> snapshots = new ArrayList<Snapshot>();
        for (String snapshotName : snapshotNames) {
            snapshots.add(getSnapshotIfExists(snapshotName));
        }

        // order the snapshots by date and choose the last as the root of the histories. It means
        // that any songs that the earlier snapshots have that is not in the latest will be ignored.
        // This is ok because these tend to be songs that have been deleted from the system.
        snapshotFilter.sortSnapshotsByDate(snapshots);
        Snapshot latestSnapshot = snapshots.get(snapshots.size() - 1);

        SnapshotsHistory history = new SnapshotsHistory();
        for (Snapshot snapshot : snapshots) {
            history.addSnapshot(snapshot.getName());
        }

        // determine the history of each song
        for (Map.Entry<Song, SongStatistics> latestStatistic : latestSnapshot
                .getStatistics()
                .entrySet()) {
            Song song = latestStatistic.getKey();
            SongHistory songHistory =
                    new SongHistory()
                            .withArtistName(song.getArtistName())
                            .withAlbumName(song.getAlbumName())
                            .withSongName(song.getName());
            for (Snapshot snapshot : snapshots) {
                songHistory.addSongStatistic(
                        snapshot.getName(),
                        snapshot.getStatistics().get(song));
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
            throw new SnapshotDoesNotExistException(snapshotName);
        }
        return snapshot;
    }

    @Override
    public final SongHistory generateSongHistory(
            String artistName,
            String albumName,
            String songName,
            List<String> snapshotNames) throws StatisticsException {
        if (snapshotNames.size() < MIN_SNAPSHOTS_FOR_HISTORY) {
            throw new InsufficientSnapshotsSpecifiedException(
                    MIN_SNAPSHOTS_FOR_HISTORY,
                    snapshotNames);
        }

        Map<String, Snapshot> snapshotsByName =
                snapshotStore.getSnapshotsWithoutStatistics(snapshotNames);
        for (String snapshotName : snapshotNames) {
            if (snapshotsByName.get(snapshotName) == null) {
                throw new SnapshotDoesNotExistException(snapshotName);
            }
        }

        Map<String, SongStatistics> allStatistics =
                snapshotStore.getSongStatisticsFromSnapshots(
                        artistName,
                        albumName,
                        songName,
                        snapshotNames);
        if (allStatistics.size() == 0) {
            throw new SongDoesNotExistForSnapshotsException(
                    artistName,
                    albumName,
                    songName,
                    snapshotNames);
        }

        List<Snapshot> snapshots = new ArrayList<Snapshot>(snapshotsByName.values());
        snapshotFilter.sortSnapshotsByDate(snapshots);

        SongHistory songHistory =
                new SongHistory()
                        .withArtistName(artistName)
                        .withAlbumName(albumName)
                        .withSongName(songName);
        for (Snapshot snapshot : snapshots) {
            songHistory.addSongStatistic(
                    snapshot.getName(),
                    allStatistics.get(snapshot.getName()));
        }

        return songHistory;
    }

    @Override
    public final List<String> getQuarterlySnapshots() throws StatisticsException {
        List<Snapshot> snapshots =
                snapshotFilter.filterByLastInQuarter(snapshotStore
                        .getSnapshotsWithoutStatistics());
        List<String> snapshotNames = new ArrayList<String>(snapshots.size());
        for (Snapshot snapshot : snapshots) {
            snapshotNames.add(snapshot.getName());
        }
        return snapshotNames;
    }
}
