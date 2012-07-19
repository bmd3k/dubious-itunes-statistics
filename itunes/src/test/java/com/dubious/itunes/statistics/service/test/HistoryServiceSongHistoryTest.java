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
import com.dubious.itunes.statistics.exception.SongDoesNotExistForSnapshotsException;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
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
public class HistoryServiceSongHistoryTest {

    @Resource(name = "mongoDbSnapshotStore")
    private SnapshotStore snapshotStore;
    @Resource(name = "snapshotService")
    private SnapshotService snapshotService;
    @Resource(name = "historyService")
    private HistoryService historyService;

    private String snapshot1 = "snapshot1";
    private String snapshot2 = "snapshot2";
    private String snapshot3 = "snapshot3";
    private String snapshot4 = "snapshot4";
    private DateTime december1 = new DateMidnight(2011, 12, 1).toDateTime();
    private DateTime december5 = new DateMidnight(2011, 12, 5).toDateTime();
    private DateTime december10 = new DateMidnight(2011, 12, 10).toDateTime();
    private DateTime december12 = new DateMidnight(2011, 12, 12).toDateTime();
    private Song song1 = new Song()
            .withArtistName("Arctic Monkeys")
            .withAlbumName("Whatever People Say I Am, That's What I'm Not")
            .withName("Mardy Bum");
    private Song song2 = new Song()
            .withArtistName("The New Pornographers")
            .withAlbumName("Mass Romantic")
            .withName("Mass Romantic");

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
     * Test error case when no snapshots are specified.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testWithNoSnapshots() throws StatisticsException {
        try {
            historyService.generateSongHistory(
                    song1.getArtistName(),
                    song1.getAlbumName(),
                    song1.getName(),
                    Collections.<String>emptyList());
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
            historyService.generateSongHistory(
                    song1.getArtistName(),
                    song1.getAlbumName(),
                    song1.getName(),
                    asList(snapshot1));
            fail("expected exception not thrown");
        } catch (InsufficientSnapshotsSpecifiedException e) {
            assertEquals(e, new InsufficientSnapshotsSpecifiedException(2, asList(snapshot1)));
        }
    }

    /**
     * Test error case where specified snapshot does not exist.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSnapshotDoesNotExist() throws StatisticsException {
        try {
            historyService.generateSongHistory(
                    song1.getArtistName(),
                    song1.getAlbumName(),
                    song1.getName(),
                    asList(snapshot1, snapshot2));
            fail("expected exception not thrown");
        } catch (SnapshotDoesNotExistException e) {
            assertEquals(e, new SnapshotDoesNotExistException(snapshot1));
        }
    }

    /**
     * Test error case where song does not exist in any of the snapshots.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSongDoesNotExistForSnapshots() throws StatisticsException {
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december1)
                .addStatistic(song2, new SongStatistics().withPlayCount(5)));
        snapshotService.writeSnapshot(new Snapshot().withName(snapshot2).withDate(december1));

        try {
            historyService.generateSongHistory(
                    song1.getArtistName(),
                    song1.getAlbumName(),
                    song1.getName(),
                    asList(snapshot1, snapshot2));
            fail("expected exception not thrown");
        } catch (SongDoesNotExistForSnapshotsException e) {
            assertEquals(e, new SongDoesNotExistForSnapshotsException(
                    song1.getArtistName(),
                    song1.getAlbumName(),
                    song1.getName(),
                    asList(snapshot1, snapshot2)));
        }
    }

    /**
     * Test retrieval of song history from 2 snapshots.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testWith2Snapshots() throws StatisticsException {
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december1)
                .addStatistic(song1, new SongStatistics().withPlayCount(5)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot2)
                .withDate(december5)
                .addStatistic(song1, new SongStatistics().withPlayCount(8)));

        assertEquals(
                new SongHistory()
                        .withSong(song1)
                        .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(5))
                        .addSongStatistic(snapshot2, new SongStatistics().withPlayCount(8)),
                historyService.generateSongHistory(
                        song1.getArtistName(),
                        song1.getAlbumName(),
                        song1.getName(),
                        asList(snapshot1, snapshot2)));
    }

    /**
     * Test retrieval of song history for more than 2 snapshots.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testWithManySnapshots() throws StatisticsException {
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december1)
                .addStatistic(song1, new SongStatistics().withPlayCount(5)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot2)
                .withDate(december5)
                .addStatistic(song1, new SongStatistics().withPlayCount(8)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot3)
                .withDate(december10)
                .addStatistic(song1, new SongStatistics().withPlayCount(8)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot4)
                .withDate(december12)
                .addStatistic(song1, new SongStatistics().withPlayCount(9)));

        // specify all snapshots
        assertEquals(
                new SongHistory()
                        .withSong(song1)
                        .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(5))
                        .addSongStatistic(snapshot2, new SongStatistics().withPlayCount(8))
                        .addSongStatistic(snapshot3, new SongStatistics().withPlayCount(8))
                        .addSongStatistic(snapshot4, new SongStatistics().withPlayCount(9)),
                historyService.generateSongHistory(
                        song1.getArtistName(),
                        song1.getAlbumName(),
                        song1.getName(),
                        asList(snapshot1, snapshot2, snapshot3, snapshot4)));

        // specify a subset of snapshots
        assertEquals(
                new SongHistory()
                        .withSong(song1)
                        .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(5))
                        .addSongStatistic(snapshot3, new SongStatistics().withPlayCount(8)),
                historyService.generateSongHistory(
                        song1.getArtistName(),
                        song1.getAlbumName(),
                        song1.getName(),
                        asList(snapshot1, snapshot3)));
    }

    /**
     * Test retrieval of song history in multi-song scenario.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testWithDifferentSongs() throws StatisticsException {
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december1)
                .addStatistic(song1, new SongStatistics().withPlayCount(5))
                .addStatistic(song2, new SongStatistics().withPlayCount(1)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot2)
                .withDate(december5)
                .addStatistic(song1, new SongStatistics().withPlayCount(8))
                .addStatistic(song2, new SongStatistics().withPlayCount(3)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot3)
                .withDate(december10)
                .addStatistic(song1, new SongStatistics().withPlayCount(8))
                .addStatistic(song2, new SongStatistics().withPlayCount(6)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot4)
                .withDate(december12)
                .addStatistic(song2, new SongStatistics().withPlayCount(12))
                .addStatistic(song1, new SongStatistics().withPlayCount(9)));

        // song 1
        assertEquals(
                new SongHistory()
                        .withSong(song1)
                        .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(5))
                        .addSongStatistic(snapshot2, new SongStatistics().withPlayCount(8))
                        .addSongStatistic(snapshot3, new SongStatistics().withPlayCount(8))
                        .addSongStatistic(snapshot4, new SongStatistics().withPlayCount(9)),
                historyService.generateSongHistory(
                        song1.getArtistName(),
                        song1.getAlbumName(),
                        song1.getName(),
                        asList(snapshot1, snapshot2, snapshot3, snapshot4)));
        // song 2
        assertEquals(
                new SongHistory()
                        .withSong(song2)
                        .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(1))
                        .addSongStatistic(snapshot2, new SongStatistics().withPlayCount(3))
                        .addSongStatistic(snapshot3, new SongStatistics().withPlayCount(6))
                        .addSongStatistic(snapshot4, new SongStatistics().withPlayCount(12)),
                historyService.generateSongHistory(
                        song2.getArtistName(),
                        song2.getAlbumName(),
                        song2.getName(),
                        asList(snapshot1, snapshot2, snapshot3, snapshot4)));
    }

    /**
     * Test scenario where one of the snapshots does not contain the song.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSnapshotMissingSong() throws StatisticsException {
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december1)
                .addStatistic(song1, new SongStatistics().withPlayCount(5)));
        snapshotService.writeSnapshot(new Snapshot().withName(snapshot2).withDate(december5));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot3)
                .withDate(december10)
                .addStatistic(song1, new SongStatistics().withPlayCount(8)));
        snapshotService.writeSnapshot(new Snapshot()
                .withName(snapshot4)
                .withDate(december12)
                .addStatistic(song1, new SongStatistics().withPlayCount(9)));

        // specify all snapshots
        assertEquals(
                new SongHistory()
                        .withSong(song1)
                        .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(5))
                        .addSongStatistic(snapshot2, null)
                        .addSongStatistic(snapshot3, new SongStatistics().withPlayCount(8))
                        .addSongStatistic(snapshot4, new SongStatistics().withPlayCount(9)),
                historyService.generateSongHistory(
                        song1.getArtistName(),
                        song1.getAlbumName(),
                        song1.getName(),
                        asList(snapshot1, snapshot2, snapshot3, snapshot4)));
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
                new SongHistory()
                        .withSong(song1)
                        .addSongStatistic(snapshot2, new SongStatistics().withPlayCount(9))
                        .addSongStatistic(snapshot1, new SongStatistics().withPlayCount(10)),
                historyService.generateSongHistory(song1.getArtistName(),
                        song1.getAlbumName(),
                        song1.getName(),
                        asList(snapshot1, snapshot2)));
        //@formatter:on
    }
}
