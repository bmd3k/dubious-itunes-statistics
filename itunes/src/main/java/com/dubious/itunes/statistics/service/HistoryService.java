package com.dubious.itunes.statistics.service;

import java.util.List;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;

/**
 * Provides historical information using statistical snapshots.
 */
public interface HistoryService {

    /**
     * Provide history of many songs consisting of information from two or more snapshots. The
     * history contains all songs that exist in the most recent snapshot given in the input.
     * 
     * @param snapshots Names of snapshots to use for the comparison.
     * @return History of all songs from the most recent snapshot.
     * @throws StatisticsException On error.
     */
    SnapshotsHistory generateSnapshotHistory(List<String> snapshots) throws StatisticsException;

    /**
     * Provide a history of a single song consisting of information from two or more snapshots.
     * 
     * @param artistName Name of the artist.
     * @param albumName Name of the album.
     * @param songName Name of the song.
     * @param snapshots Names of snapshots to use for the history.
     * @return History of a single song.
     * @throws StatisticsException On error.
     */
    SongHistory generateSongHistory(
            String artistName,
            String albumName,
            String songName,
            List<String> snapshots) throws StatisticsException;

    /**
     * Get a "quarterly" set of snapshots. There is one snapshot returned representing each quarter
     * of a year. The snapshot returned is generally the last snapshot of the quarter although
     * sometimes exceptions are made.
     * 
     * @return The "quarterly" set of snapshots.
     * @throws StatisticsException On error.
     */
    List<String> getQuarterlySnapshots() throws StatisticsException;
}
