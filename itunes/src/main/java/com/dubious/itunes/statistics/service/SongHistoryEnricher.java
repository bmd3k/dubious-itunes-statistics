package com.dubious.itunes.statistics.service;

import java.util.HashMap;
import java.util.Map;

import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;

/**
 * Logic for enriching song history with calculated data.
 */
public class SongHistoryEnricher {

    /**
     * Enrich song history. Currently this involves the following:
     * 
     * <ul>
     * <li>Calculating difference data</li>
     * <li>Creating SongStatistics where none existed before</li>
     * </ul>
     * 
     * @param songHistory The song history to enrich.
     */
    public final void enrichSongHistory(SongHistory songHistory) {

        // note that we are relying on the fact that the underlying Map of the SongHistory object is
        // a LinkedHashMap and has well-defined order.
        Integer previousPlayCount = null;
        Map<String, SongStatistics> songStatisticsToAdd = new HashMap<String, SongStatistics>();
        for (String key : songHistory.getSongStatistics().keySet()) {
            Integer playCount = 0;
            SongStatistics songStatistics = songHistory.getSongStatistics().get(key);
            if (songStatistics != null && songStatistics.getPlayCount() != null) {
                playCount = songStatistics.getPlayCount();
            } else {
                songStatistics = new SongStatistics().withPlayCount(0);
                songStatisticsToAdd.put(key, songStatistics);
            }
            // if the first element in the map then the difference is the play count, otherwise
            // it is the actual difference between the previous and current play count.
            Integer difference = playCount;
            if (previousPlayCount != null) {
                difference = playCount - previousPlayCount;
            }
            songStatistics.withDifference(difference);

            previousPlayCount = playCount;
        }

        // add any new entries into the original map
        songHistory.addSongStatistics(songStatisticsToAdd);
    }

}
