package com.dubious.itunes.statistics.service;

import java.util.Map;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;

/**
 * Implementation of {@link SnapshotSynchronizationAdjustment}.
 */
public class SnapshotSynchronizationAdjuster implements SnapshotSynchronizationAdjustment {

    @Override
    public final Snapshot adjustSnapshotForSynchronization(Snapshot snapshot) {
        Snapshot adjustedSnapshot =
                new Snapshot().withName(snapshot.getName()).withDate(snapshot.getDate());
        for (Map.Entry<Song, SongStatistics> statistic : snapshot.getStatistics().entrySet()) {
            Song song = statistic.getKey();
            if ((song.getAlbumName() == null || song.getAlbumName().length() == 0)
                    && song.getTrackNumber() != null) {
                adjustedSnapshot.addStatistic(new Song()
                        .withArtistName(song.getArtistName())
                        .withAlbumName(song.getAlbumName())
                        .withName(song.getName()), statistic.getValue());
            } else {
                adjustedSnapshot.addStatistic(song, statistic.getValue());
            }
        }

        return adjustedSnapshot;
    }
}
