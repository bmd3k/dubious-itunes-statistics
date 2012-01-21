package com.dubious.itunes.statistics.service;

import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;

/**
 * Enrich history with more analytical information.
 */
public interface AnalysisService {

    /**
     * Enrich a snapshots history with other analysis.
     * 
     * @param history The history to analyze.
     * @return Enriched ersion of history.
     */
    SnapshotsHistory enrichSnapshotsHistory(SnapshotsHistory history);

    /**
     * Enrich a song history with other analysis.
     * 
     * @param songHistory The history to analyze.
     * @return Enriched version of songHistory.
     */
    SongHistory enrichSongHistory(SongHistory songHistory);
}
