package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.InsufficientSnapshotsSpecifiedException;
import com.dubious.itunes.statistics.exception.SnapshotDoesNotExistException;
import com.dubious.itunes.statistics.exception.SongDoesNotExistForSnapshotsException;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.HistoryService;
import com.dubious.itunes.statistics.service.HistoryServiceImpl;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.StoreException;
import com.dubious.itunes.statistics.store.mongodb.MongoDbDataSource;
import com.dubious.itunes.statistics.store.mongodb.MongoDbSnapshotStore;
import com.dubious.itunes.statistics.store.mongodb.MongoDbStoreException;

/**
 * Tests of {@link HistoryService#generateSnapshotHistory(java.util.List)}.
 */
public class HistoryServiceSongHistoryTest {

    private static SnapshotStore snapshotStore;
    private static HistoryService snapshotService;

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
     * Setup at the class level.
     * 
     * @throws MongoDbStoreException On unexpected error.
     */
    @BeforeClass
    public static void beforeClass() throws MongoDbStoreException {
        snapshotStore = new MongoDbSnapshotStore(new MongoDbDataSource("localhost", "testdb"));
        snapshotService = new HistoryServiceImpl(snapshotStore);
    }

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
            snapshotService.generateSongHistory(
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
            snapshotService.generateSongHistory(
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
            snapshotService.generateSongHistory(
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
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december1)
                .addStatistic(song2, new SongStatistics().withPlayCount(5)));
        snapshotStore.writeSnapshot(new Snapshot().withName(snapshot2).withDate(december1));

        try {
            snapshotService.generateSongHistory(
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
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december1)
                .addStatistic(song1, new SongStatistics().withPlayCount(5)));
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot2)
                .withDate(december5)
                .addStatistic(song1, new SongStatistics().withPlayCount(8)));

        assertEquals(
                new SongHistory()
                        .withSong(song1)
                        .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(5))
                        .addSongStatistics(snapshot2, new SongStatistics().withPlayCount(8)),
                snapshotService.generateSongHistory(
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
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december1)
                .addStatistic(song1, new SongStatistics().withPlayCount(5)));
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot2)
                .withDate(december5)
                .addStatistic(song1, new SongStatistics().withPlayCount(8)));
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot3)
                .withDate(december10)
                .addStatistic(song1, new SongStatistics().withPlayCount(8)));
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot4)
                .withDate(december12)
                .addStatistic(song1, new SongStatistics().withPlayCount(9)));

        // specify all snapshots
        assertEquals(
                new SongHistory()
                        .withSong(song1)
                        .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(5))
                        .addSongStatistics(snapshot2, new SongStatistics().withPlayCount(8))
                        .addSongStatistics(snapshot3, new SongStatistics().withPlayCount(8))
                        .addSongStatistics(snapshot4, new SongStatistics().withPlayCount(9)),
                snapshotService.generateSongHistory(
                        song1.getArtistName(),
                        song1.getAlbumName(),
                        song1.getName(),
                        asList(snapshot1, snapshot2, snapshot3, snapshot4)));

        // specify a subset of snapshots
        assertEquals(
                new SongHistory()
                        .withSong(song1)
                        .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(5))
                        .addSongStatistics(snapshot3, new SongStatistics().withPlayCount(8)),
                snapshotService.generateSongHistory(
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
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december1)
                .addStatistic(song1, new SongStatistics().withPlayCount(5))
                .addStatistic(song2, new SongStatistics().withPlayCount(1)));
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot2)
                .withDate(december5)
                .addStatistic(song1, new SongStatistics().withPlayCount(8))
                .addStatistic(song2, new SongStatistics().withPlayCount(3)));
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot3)
                .withDate(december10)
                .addStatistic(song1, new SongStatistics().withPlayCount(8))
                .addStatistic(song2, new SongStatistics().withPlayCount(6)));
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot4)
                .withDate(december12)
                .addStatistic(song2, new SongStatistics().withPlayCount(12))
                .addStatistic(song1, new SongStatistics().withPlayCount(9)));

        // song 1
        assertEquals(
                new SongHistory()
                        .withSong(song1)
                        .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(5))
                        .addSongStatistics(snapshot2, new SongStatistics().withPlayCount(8))
                        .addSongStatistics(snapshot3, new SongStatistics().withPlayCount(8))
                        .addSongStatistics(snapshot4, new SongStatistics().withPlayCount(9)),
                snapshotService.generateSongHistory(
                        song1.getArtistName(),
                        song1.getAlbumName(),
                        song1.getName(),
                        asList(snapshot1, snapshot2, snapshot3, snapshot4)));
        // song 2
        assertEquals(
                new SongHistory()
                        .withSong(song2)
                        .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(1))
                        .addSongStatistics(snapshot2, new SongStatistics().withPlayCount(3))
                        .addSongStatistics(snapshot3, new SongStatistics().withPlayCount(6))
                        .addSongStatistics(snapshot4, new SongStatistics().withPlayCount(12)),
                snapshotService.generateSongHistory(
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
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december1)
                .addStatistic(song1, new SongStatistics().withPlayCount(5)));
        snapshotStore.writeSnapshot(new Snapshot().withName(snapshot2).withDate(december5));
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot3)
                .withDate(december10)
                .addStatistic(song1, new SongStatistics().withPlayCount(8)));
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot4)
                .withDate(december12)
                .addStatistic(song1, new SongStatistics().withPlayCount(9)));

        // specify all snapshots
        assertEquals(
                new SongHistory()
                        .withSong(song1)
                        .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(5))
                        .addSongStatistics(snapshot2, null)
                        .addSongStatistics(snapshot3, new SongStatistics().withPlayCount(8))
                        .addSongStatistics(snapshot4, new SongStatistics().withPlayCount(9)),
                snapshotService.generateSongHistory(
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
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december5)
                .addStatistic(song1, new SongStatistics().withPlayCount(10)));
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot2)
                .withDate(december1)
                .addStatistic(song1, new SongStatistics().withPlayCount(9)));

        //@formatter:off
        assertEquals(
                new SongHistory()
                        .withSong(song1)
                        .addSongStatistics(snapshot2, new SongStatistics().withPlayCount(9))
                        .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(10)),
                snapshotService.generateSongHistory(song1.getArtistName(),
                        song1.getAlbumName(),
                        song1.getName(),
                        asList(snapshot1, snapshot2)));
        //@formatter:on
    }
}
