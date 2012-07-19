package com.dubious.itunes.statistics.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Statistical history of a song.
 */
public class SongHistory extends SongKey<SongHistory> {

    private Map<String, SongStatistics> songStatistics;

    /**
     * Constructor.
     */
    public SongHistory() {
        this.songStatistics = new LinkedHashMap<String, SongStatistics>();
    }

    @Override
    public final SongHistory getThis() {
        return this;
    }

    /**
     * Add statistics for the song from a snapshot.
     * 
     * @param snapshotName The name of the snapshot.
     * @param songStatistics The statistics of the song from the snapshot.
     * @return This.
     */
    public final SongHistory addSongStatistic(String snapshotName, SongStatistics songStatistics) {
        this.songStatistics.put(snapshotName, songStatistics);
        return this;
    }

    /**
     * Add many statistics for the song from a snapshot.
     * 
     * @param newSongStatistics The statistics.
     * @return This.
     */
    public final SongHistory addSongStatistics(Map<String, SongStatistics> newSongStatistics) {
        this.songStatistics.putAll(newSongStatistics);
        return this;
    }

    /**
     * Get the map of snapshot to statistics.
     * 
     * @return The map of snapshot to statistics.
     */
    public final Map<String, SongStatistics> getSongStatistics() {
        return Collections.unmodifiableMap(songStatistics);
    }
}
