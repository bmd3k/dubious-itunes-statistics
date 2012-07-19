package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.AnalysisService.Order;
import com.dubious.itunes.statistics.service.SongHistorySorter;

/**
 * Tests of {@link SongHistorySorter}.
 */
public class SongHistorySorterTest {

    private SongHistorySorter sorter;

    // the snapshot that will be used for testing ordering functionality
    // if ordered by snapshot1 playcount: song2,song4,song3,song1
    // if ordered by snapshot2 playcount: song4,song2,song3,song1
    // if ordered by snapshot3 playcount: song2,song4,song1,song3 **
    // if ordered by difference between snapshot2 and snapshot1: song4,song2/3,song2/3,song1
    // if ordered by difference between snapshot3 and snapshot2: song1/2,song1/2,song3/4,song3/4
    // if ordered by difference between snapshot3 and snapshot1: song4,song2,song1,song3 **
    private List<String> snapshots = asList("snapshot1", "snapshot2", "snapshot3");
    private List<SongHistory> songHistories = Arrays.asList(
            new SongHistory()
                    .withSongName("song1")
                    .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(1))
                    .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(1))
                    .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(4)),
            new SongHistory()
                    .withSongName("song2")
                    .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(5))
                    .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(6))
                    .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(9)),
            new SongHistory()
                    .withSongName("song3")
                    .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(2))
                    .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(3))
                    .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(3)),
            new SongHistory()
                    .withSongName("song4")
                    .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(3))
                    .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(8))
                    .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(8)));

    /**
     * Test setup.
     */
    @Before
    public final void before() {
        sorter = new SongHistorySorter();
    }

    /**
     * Test order by play count.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testOrderByPlayCount() throws StatisticsException {
        sorter.sortSongHistory(snapshots, songHistories, Order.PlayCount);
        //@formatter:off
        assertEquals(Arrays.asList(
                new SongHistory()
                        .withSongName("song2")
                        .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(5))
                        .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(6))
                        .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(9)),
                new SongHistory()
                        .withSongName("song4")
                        .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(3))
                        .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(8))
                        .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(8)),
                new SongHistory()
                        .withSongName("song1")
                        .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(1))
                        .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(1))
                        .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(4)),
                new SongHistory()
                        .withSongName("song3")
                        .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(2))
                        .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(3))
                        .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(3))), 
                songHistories);
        //@formatter:on
    }

    /**
     * Test order by difference.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testOrderByDifference() throws StatisticsException {
        sorter.sortSongHistory(snapshots, songHistories, Order.Difference);
        //@formatter:off
        assertEquals(Arrays.asList(
                new SongHistory()
                        .withSongName("song4")
                        .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(3))
                        .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(8))
                        .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(8)),
                new SongHistory()
                        .withSongName("song2")
                        .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(5))
                        .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(6))
                        .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(9)),
                new SongHistory()
                        .withSongName("song1")
                        .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(1))
                        .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(1))
                        .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(4)),
                new SongHistory()
                        .withSongName("song3")
                        .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(2))
                        .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(3))
                        .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(3))), 
                songHistories);
        //@formatter:on
    }
}
