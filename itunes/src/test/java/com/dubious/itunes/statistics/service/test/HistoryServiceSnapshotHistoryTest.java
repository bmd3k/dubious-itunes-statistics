package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;

import javax.annotation.Resource;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.InsufficientSnapshotsSpecifiedException;
import com.dubious.itunes.statistics.exception.SnapshotDoesNotExistException;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.HistoryService;
import com.dubious.itunes.statistics.service.SnapshotService;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.StoreException;

/**
 * Tests of {@link HistoryService#generateSnapshotHistory(java.util.List)}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/dubious/itunes/test/itunes-test-context.xml" })
public class HistoryServiceSnapshotHistoryTest {

    @Resource(name = "mongoDbSnapshotStore")
    private SnapshotStore snapshotStore;
    @Resource(name = "snapshotService")
    private SnapshotService snapshotService;
    @Resource(name = "historyService")
    private HistoryService historyService;

    private String snapshot1 = "snapshot1";
    private String snapshot2 = "snapshot2";
    private String snapshot3 = "snapshot3";
    private DateTime december1 = new DateMidnight(2011, 12, 1).toDateTime();
    private DateTime december5 = new DateMidnight(2011, 12, 5).toDateTime();
    private DateTime december10 = new DateMidnight(2011, 12, 10).toDateTime();
    private Song song1 = new Song()
            .withArtistName("Arctic Monkeys")
            .withAlbumName("Whatever People Say I Am, That's What I'm Not")
            .withName("Mardy Bum");
    private Song song2 = new Song()
            .withArtistName("Nada Surf")
            .withAlbumName("The Weight is a Gift")
            .withName("Blankest Year");
    private Song song3 = new Song()
            .withArtistName("Death From Above 1979")
            .withAlbumName("You're A Woman I'm A Machine")
            .withName("Going Steady");

    /**
     * Tear down tests.
     * 
     * @throws StoreException On unexpected error.
     */
    @After
    public final void after() throws StoreException {
        snapshotStore.deleteAll();
    }

    /**
     * Test error case where specified snapshot does not exist.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSnapshotDoesNotExist() throws StatisticsException {
        try {
            historyService.generateSnapshotHistory(asList(
                    "091201 - Music.txt",
                    "111201 - Music.txt"));
            fail("expected exception not thrown");
        } catch (SnapshotDoesNotExistException e) {
            assertEquals(e, new SnapshotDoesNotExistException("091201 - Music.txt"));
        }
    }

    /**
     * Test error case when no snapshots are specified.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testWithNoSnapshots() throws StatisticsException {
        try {
            historyService.generateSnapshotHistory(Collections.<String>emptyList());
            fail("expected exception not thrown");
        } catch (InsufficientSnapshotsSpecifiedException e) {
            assertEquals(
                    e,
                    new InsufficientSnapshotsSpecifiedException(2, Collections
                            .<String>emptyList()));
        }
    }

    /**
     * Test error case when only one snapshot is specified.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testWith1Snapshot() throws StatisticsException {
        try {
            historyService.generateSnapshotHistory(asList("111201 - Music.txt"));
            fail("expected exception not thrown");
        } catch (InsufficientSnapshotsSpecifiedException e) {
            assertEquals(e, new InsufficientSnapshotsSpecifiedException(
                    2,
                    asList("111201 - Music.txt")));
        }
    }

    /**
     * Test comparing two snapshots with same songs in same order.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testWith2Snapshots() throws StatisticsException {
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december1)
                .addStatistic(song1, new SongStatistics().withPlayCount(61))
                .addStatistic(song2, new SongStatistics().withPlayCount(56))
                .addStatistic(song3, new SongStatistics().withPlayCount(55)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot2)
                .withDate(december5)
                .addStatistic(song1, new SongStatistics().withPlayCount(62))
                .addStatistic(song2, new SongStatistics().withPlayCount(59))
                .addStatistic(song3, new SongStatistics().withPlayCount(58)));

        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .addSnapshot(snapshot1)
                        .addSnapshot(snapshot2)
                        .addSongHistory(new SongHistory()
                                .withSong(song1)
                                .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(61))
                                .addSongStatistic(snapshot2, new SongStatistics().withPlayCount(62)))
                        .addSongHistory(new SongHistory()
                                .withSong(song2)
                                .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(56))
                                .addSongStatistic(snapshot2, new SongStatistics().withPlayCount(59)))
                        .addSongHistory(new SongHistory()
                                .withSong(song3)
                                .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(55))
                                .addSongStatistic(snapshot2, new SongStatistics().withPlayCount(58))),
                historyService.generateSnapshotHistory(asList(snapshot1, snapshot2)));
        //@formatter:on
    }

    /**
     * Test comparing multiple snapshots where the latest snapshot has songs not in the earlier
     * ones. These songs should be considered in the history.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testLatestHasUniqueSongs() throws StatisticsException {
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december1)
                .addStatistic(song1, new SongStatistics().withPlayCount(61))
                .addStatistic(song2, new SongStatistics().withPlayCount(56)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot2)
                .withDate(december5)
                .addStatistic(song1, new SongStatistics().withPlayCount(62))
                .addStatistic(song2, new SongStatistics().withPlayCount(59)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot3)
                .withDate(december10)
                .addStatistic(song1, new SongStatistics().withPlayCount(64))
                .addStatistic(song3, new SongStatistics().withPlayCount(60))
                .addStatistic(song2, new SongStatistics().withPlayCount(59)));

        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .addSnapshot(snapshot1)
                        .addSnapshot(snapshot2)
                        .addSnapshot(snapshot3)
                        .addSongHistory(new SongHistory()
                                .withSong(song1)
                                .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(61))
                                .addSongStatistic(snapshot2, new SongStatistics().withPlayCount(62))
                                .addSongStatistic(snapshot3, new SongStatistics().withPlayCount(64)))
                        .addSongHistory(new SongHistory()
                                .withSong(song3)
                                .addSongStatistic(snapshot1, null)
                                .addSongStatistic(snapshot2, null)
                                .addSongStatistic(snapshot3, new SongStatistics().withPlayCount(60)))
                        .addSongHistory(new SongHistory()
                                .withSong(song2)
                                .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(56))
                                .addSongStatistic(snapshot2, new SongStatistics().withPlayCount(59))
                                .addSongStatistic(snapshot3, new SongStatistics().withPlayCount(59))),
                historyService.generateSnapshotHistory(asList(snapshot1, snapshot2, snapshot3)));
        //@formatter:on
    }

    /**
     * Test comparing multiple snapshots where the latest does not contain all the songs in the
     * earlier ones. These songs should not be contained in the history.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testLatestMissingSongs() throws StatisticsException {
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december1)
                .addStatistic(song1, new SongStatistics().withPlayCount(61))
                .addStatistic(song2, new SongStatistics().withPlayCount(56)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot2)
                .withDate(december5)
                .addStatistic(song1, new SongStatistics().withPlayCount(62))
                .addStatistic(song3, new SongStatistics().withPlayCount(59)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot3)
                .withDate(december10)
                .addStatistic(song1, new SongStatistics().withPlayCount(64)));

        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .addSnapshot(snapshot1)
                        .addSnapshot(snapshot2)
                        .addSnapshot(snapshot3)
                        .addSongHistory(new SongHistory()
                                .withSong(song1)
                                .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(61))
                                .addSongStatistic(snapshot2, new SongStatistics().withPlayCount(62))
                                .addSongStatistic(snapshot3, new SongStatistics().withPlayCount(64))),
                historyService.generateSnapshotHistory(asList(snapshot1, snapshot2, snapshot3)));
        //@formatter:on
    }

    /**
     * Test comparing two snapshots where the order of songs is different. The order of the latest
     * is the one that should be honoured.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testDifferentSongOrders() throws StatisticsException {
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december1)
                .addStatistic(song1, new SongStatistics().withPlayCount(15))
                .addStatistic(song2, new SongStatistics().withPlayCount(10))
                .addStatistic(song3, new SongStatistics().withPlayCount(9)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot2)
                .withDate(december5)
                .addStatistic(song1, new SongStatistics().withPlayCount(17))
                .addStatistic(song3, new SongStatistics().withPlayCount(16))
                .addStatistic(song2, new SongStatistics().withPlayCount(14)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot3)
                .withDate(december10)
                .addStatistic(song3, new SongStatistics().withPlayCount(20))
                .addStatistic(song2, new SongStatistics().withPlayCount(19))
                .addStatistic(song1, new SongStatistics().withPlayCount(18)));

        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .addSnapshot(snapshot1)
                        .addSnapshot(snapshot2)
                        .addSnapshot(snapshot3)
                        .addSongHistory(new SongHistory()
                                .withSong(song3)
                                .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(9))
                                .addSongStatistic(snapshot2, new SongStatistics().withPlayCount(16))
                                .addSongStatistic(snapshot3, new SongStatistics().withPlayCount(20)))
                        .addSongHistory(new SongHistory()
                                .withSong(song2)
                                .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(10))
                                .addSongStatistic(snapshot2, new SongStatistics().withPlayCount(14))
                                .addSongStatistic(snapshot3, new SongStatistics().withPlayCount(19)))
                        .addSongHistory(new SongHistory()
                                .withSong(song1)
                                .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(15))
                                .addSongStatistic(snapshot2, new SongStatistics().withPlayCount(17))
                                .addSongStatistic(snapshot3, new SongStatistics().withPlayCount(18))),
                historyService.generateSnapshotHistory(asList(snapshot1, snapshot2, snapshot3)));
        //@formatter:on
    }

    /**
     * Test that the order returned in the history is proper, no matter the order given as input.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSnapshotOrdering() throws StatisticsException {
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december5)
                .addStatistic(song1, new SongStatistics().withPlayCount(10)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot2)
                .withDate(december1)
                .addStatistic(song1, new SongStatistics().withPlayCount(9)));

        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .addSnapshot(snapshot2)
                        .addSnapshot(snapshot1)
                        .addSongHistory(new SongHistory()
                                .withSong(song1)
                                .addSongStatistic(snapshot2, new SongStatistics().withPlayCount(9))
                                .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(10))),
                historyService.generateSnapshotHistory(asList(snapshot1, snapshot2)));
        //@formatter:on
    }
}
