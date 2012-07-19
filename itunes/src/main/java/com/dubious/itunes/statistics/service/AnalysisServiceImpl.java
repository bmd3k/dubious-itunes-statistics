package com.dubious.itunes.statistics.service;

import java.util.ArrayList;
import java.util.List;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;

/**
 * Implementation of snapshot-based statistical analysis.
 */
public class AnalysisServiceImpl implements AnalysisService {

    private HistoryService historyService;
    private SongHistoryEnricher songHistoryEnricher;
    private SongHistorySorter songHistorySorter;

    /**
     * Constructor.
     * 
     * @param historyService {@link HistoryService} to inject.
     * @param songHistoryEnricher {@link SongHistoryEnricher} to inject.
     * @param songHistorySorter {@link SongHistorySorting} to inject.
     */
    public AnalysisServiceImpl(HistoryService historyService,
            SongHistoryEnricher songHistoryEnricher,
            SongHistorySorter songHistorySorter) {
        this.historyService = historyService;
        this.songHistoryEnricher = songHistoryEnricher;
        this.songHistorySorter = songHistorySorter;
    }

    @Override
    public final SnapshotsHistory getEnrichedSnapshotsHistory(List<String> snapshots, Order order)
            throws StatisticsException {
        SnapshotsHistory snapshotsHistoryFromStore =
                historyService.generateSnapshotHistory(snapshots);
        // build enriched and ordered song history list
        List<SongHistory> songHistories =
                new ArrayList<SongHistory>(snapshotsHistoryFromStore.getSongHistories().size());
        for (SongHistory songHistory : snapshotsHistoryFromStore.getSongHistories()) {
            songHistoryEnricher.enrichSongHistory(songHistory);
            songHistories.add(songHistory);
        }
        songHistorySorter.sortSongHistory(
                snapshotsHistoryFromStore.getSnapshots(),
                songHistories,
                order);

        return new SnapshotsHistory()
                .addSnapshots(snapshotsHistoryFromStore.getSnapshots())
                .addSongHistories(songHistories);
    }

    @Override
    public final SongHistory getEnrichedSongHistory(
            String artistName,
            String albumName,
            String songName,
            List<String> snapshots) throws StatisticsException {
        SongHistory songHistory =
                historyService.generateSongHistory(artistName, albumName, songName, snapshots);
        songHistoryEnricher.enrichSongHistory(songHistory);
        return songHistory;
    }
}
