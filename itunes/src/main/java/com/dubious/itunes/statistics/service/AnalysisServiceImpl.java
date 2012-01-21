package com.dubious.itunes.statistics.service;

import java.util.Map;

import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;

/**
 * Implementation of snapshot-based statistical analysis.
 */
public class AnalysisServiceImpl implements AnalysisService {

    @Override
    public final SnapshotsHistory enrichSnapshotsHistory(SnapshotsHistory history) {
        SnapshotsHistory newHistory =
                new SnapshotsHistory().addSnapshots(history.getSnapshots());
        for (SongHistory songHistory : history.getSongHistories()) {
            newHistory.addSongHistory(enrichSongHistory(songHistory));
        }

        return newHistory;
    }

    @Override
    public final SongHistory enrichSongHistory(SongHistory songHistory) {

        SongHistory newSongHistory =
                new SongHistory()
                        .withArtistName(songHistory.getArtistName())
                        .withAlbumName(songHistory.getAlbumName())
                        .withSongName(songHistory.getSongName());

        // note that we are relying on the fact that the underlying Map of the SongHistory object is
        // a LinkedHashMap and has well-defined order.
        Integer previousPlayCount = null;
        for (Map.Entry<String, SongStatistics> entry : songHistory
                .getSongStatistics()
                .entrySet()) {
            Integer playCount = 0;
            if (entry.getValue() != null && entry.getValue().getPlayCount() != null) {
                playCount = entry.getValue().getPlayCount();
            }
            // if the first element in the map then the difference is the play count, otherwise
            // it is the actual difference between the previous and current play count.
            Integer difference = playCount;
            if (previousPlayCount != null) {
                difference = playCount - previousPlayCount;
            }

            newSongHistory.addSongStatistics(
                    entry.getKey(),
                    new SongStatistics().withPlayCount(playCount).withDifference(difference));
            previousPlayCount = playCount;
        }

        return newSongHistory;
    }
}
