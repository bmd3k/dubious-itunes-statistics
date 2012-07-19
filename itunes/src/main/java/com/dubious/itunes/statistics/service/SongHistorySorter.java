package com.dubious.itunes.statistics.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.exception.UnexpectedStatisticsException;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.service.AnalysisService.Order;

/**
 * Logic for sorting {@link SongHistory} objects.
 */
public class SongHistorySorter {

    /**
     * Sort song history based on given order.
     * 
     * @param snapshots The snapshots involved in the history.
     * @param songs The song histories to sort.
     * @param order The order in which to sort the histories.
     * @throws StatisticsException On error.
     */
    public final void sortSongHistory(
            List<String> snapshots,
            List<SongHistory> songs,
            Order order) throws StatisticsException {

        Comparator<SongHistory> comparator = null;
        if (order == Order.PlayCount) {
            comparator = new PlayCountComparator(snapshots.get(snapshots.size() - 1));
        } else if (order == Order.Difference) {
            comparator =
                    new DifferenceComparator(
                            snapshots.get(0),
                            snapshots.get(snapshots.size() - 1));
        } else {
            throw new UnexpectedStatisticsException("Unknown Order type specified");
        }

        Collections.sort(songs, comparator);
    }

}
