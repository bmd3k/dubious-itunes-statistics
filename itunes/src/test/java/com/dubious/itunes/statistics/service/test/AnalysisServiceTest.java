package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.AnalysisService;
import com.dubious.itunes.statistics.service.AnalysisService.Order;
import com.dubious.itunes.statistics.service.SnapshotService;
import com.dubious.itunes.statistics.store.SnapshotStore;

/**
 * Integration-level tests of {@link AnalysisService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/dubious/itunes/test/itunes-test-context.xml" })
public class AnalysisServiceTest {

    @Resource(name = "snapshotService")
    private SnapshotService snapshotService;
    @Resource(name = "mongoDbSnapshotStore")
    private SnapshotStore snapshotStore;
    @Resource(name = "analysisService")
    private AnalysisService analysisService;

    private DateTime today = new DateMidnight().toDateTime();
    private DateTime tomorrow = new DateMidnight().toDateTime();

    /**
     * Test clean up.
     * 
     * @throws StatisticsException On error.
     */
    @After
    public final void after() throws StatisticsException {
        snapshotStore.deleteAll();
    }

    /**
     * Test enrich with many snapshots.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testEnrich() throws StatisticsException {
        snapshotService.writeSnapshot(new Snapshot()
                .withName("snapshot1")
                .withDate(today)
                .addStatistic(
                        new Song()
                                .withArtistName("artist")
                                .withAlbumName("album")
                                .withName("song"),
                        new SongStatistics().withPlayCount(2)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName("snapshot2")
                .withDate(tomorrow)
                .addStatistic(
                        new Song()
                                .withArtistName("artist")
                                .withAlbumName("album")
                                .withName("song"),
                        new SongStatistics().withPlayCount(5)));

        // @formatter:off
        assertEquals(new SnapshotsHistory()
                .addSnapshots(asList("snapshot1", "snapshot2"))
                .addSongHistory(new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistic(
                                "snapshot1",
                                new SongStatistics().withPlayCount(2).withDifference(2))
                        .addSongStatistic(
                                "snapshot2",
                                new SongStatistics().withPlayCount(5).withDifference(3))),
            analysisService.getEnrichedSnapshotsHistory(
                    asList("snapshot1", "snapshot2"), 
                    Order.PlayCount));
        //@formatter:on
    }
}
