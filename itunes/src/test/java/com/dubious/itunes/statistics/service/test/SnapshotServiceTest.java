package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dubious.itunes.statistics.exception.InsufficientSnapshotsSpecifiedException;
import com.dubious.itunes.statistics.exception.SnapshotDoesNotExistException;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.HistoryService;
import com.dubious.itunes.statistics.service.HistoryServiceImpl;
import com.dubious.itunes.statistics.store.ReadOnlySnapshotStore;
import com.dubious.itunes.statistics.store.file.FileSnapshotStore;
import com.dubious.itunes.statistics.store.file.FileStoreProperties;

/**
 * Tests of the {@link HistoryService}.
 */
public class SnapshotServiceTest {

    private static FileStoreProperties fileStoreProperties;
    private static ReadOnlySnapshotStore snapshotFileStore;
    private static HistoryService snapshotService;

    /**
     * Setup at the class level.
     */
    @BeforeClass
    public static void beforeClass() {
        fileStoreProperties = new FileStoreProperties("test_files", "UTF-16");
        snapshotFileStore = new FileSnapshotStore(fileStoreProperties);
        snapshotService = new HistoryServiceImpl(snapshotFileStore);
    }

    /**
     * Test error case where specified snapshot does not exist.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSnapshotDoesNotExist() throws StatisticsException {
        try {
            snapshotService.compareSnapshots(asList("091201 - Music.txt", "111201 - Music.txt"));
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
            snapshotService.compareSnapshots(Collections.<String>emptyList());
            fail("expected exception not thrown");
        } catch (InsufficientSnapshotsSpecifiedException e) {
            assertEquals(e,
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
            snapshotService.compareSnapshots(asList("111201 - Music.txt"));
            fail("expected exception not thrown");
        } catch (InsufficientSnapshotsSpecifiedException e) {
            assertEquals(e, new InsufficientSnapshotsSpecifiedException(2,
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
        String snapshot1 = "111201 - Music.txt";
        String snapshot2 = "111205 - Music.txt";
        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .addSnapshot(snapshot1)
                        .addSnapshot(snapshot2)
                        .addSongHistory(new SongHistory()
                                .withArtistName("Arctic Monkeys")
                                .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                                .withSongName("Mardy Bum")
                                .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(61))
                                .addSongStatistics(snapshot2, new SongStatistics().withPlayCount(62)))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Nada Surf")
                                .withAlbumName("The Weight is a Gift")
                                .withSongName("Blankest Year")
                                .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(56))
                                .addSongStatistics(snapshot2, new SongStatistics().withPlayCount(59)))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Death From Above 1979")
                                .withAlbumName("You're A Woman I'm A Machine")
                                .withSongName("Going Steady")
                                .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(55))
                                .addSongStatistics(snapshot2, new SongStatistics().withPlayCount(58))),
                snapshotService.compareSnapshots(asList(snapshot1, snapshot2)));
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
        String snapshot1 = "101130 - Music.txt";
        String snapshot2 = "101201 - Music.txt";
        String snapshot3 = "111201 - Music.txt";
        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .addSnapshot(snapshot1)
                        .addSnapshot(snapshot2)
                        .addSnapshot(snapshot3)
                        .addSongHistory(new SongHistory()
                                .withArtistName("Arctic Monkeys")
                                .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                                .withSongName("Mardy Bum")
                                .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(34))
                                .addSongStatistics(snapshot2, new SongStatistics().withPlayCount(35))
                                .addSongStatistics(snapshot3, new SongStatistics().withPlayCount(61)))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Nada Surf")
                                .withAlbumName("The Weight is a Gift")
                                .withSongName("Blankest Year")
                                .addSongStatistics(snapshot1, null)
                                .addSongStatistics(snapshot2, null)
                                .addSongStatistics(snapshot3, new SongStatistics().withPlayCount(56)))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Death From Above 1979")
                                .withAlbumName("You're A Woman I'm A Machine")
                                .withSongName("Going Steady")
                                .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(30))
                                .addSongStatistics(snapshot2, new SongStatistics().withPlayCount(30))
                                .addSongStatistics(snapshot3, new SongStatistics().withPlayCount(55))),
                snapshotService.compareSnapshots(asList(snapshot1, snapshot2, snapshot3)));
        //@formatter:on
    }

    /**
     * Test comparing multiple snapshots where the latest does not contain all the songs in the
     * earlier ones. These songs should not be contained in the history.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testNotLatestHasUniqueSongs() throws StatisticsException {

        String snapshot1 = "111205 - Music.txt";
        String snapshot2 = "111206 - Music.txt";
        String snapshot3 = "111207 - Music.txt";
        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .addSnapshot(snapshot1)
                        .addSnapshot(snapshot2)
                        .addSnapshot(snapshot3)
                        .addSongHistory(new SongHistory()
                                .withArtistName("Death From Above 1979")
                                .withAlbumName("You're A Woman I'm A Machine")
                                .withSongName("Going Steady")
                                .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(58))
                                .addSongStatistics(snapshot2, new SongStatistics().withPlayCount(80))
                                .addSongStatistics(snapshot3, new SongStatistics().withPlayCount(81)))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Nada Surf")
                                .withAlbumName("The Weight is a Gift")
                                .withSongName("Blankest Year")
                                .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(59))
                                .addSongStatistics(snapshot2, new SongStatistics().withPlayCount(59))
                                .addSongStatistics(snapshot3, new SongStatistics().withPlayCount(63))),
                snapshotService.compareSnapshots(asList(snapshot1, snapshot2, snapshot3)));
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

        String snapshot1 = "111201 - Music.txt";
        String snapshot2 = "111205 - Music.txt";
        String snapshot3 = "111206 - Music.txt";
        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .addSnapshot(snapshot1)
                        .addSnapshot(snapshot2)
                        .addSnapshot(snapshot3)
                        .addSongHistory(new SongHistory()
                                .withArtistName("Death From Above 1979")
                                .withAlbumName("You're A Woman I'm A Machine")
                                .withSongName("Going Steady")
                                .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(55))
                                .addSongStatistics(snapshot2, new SongStatistics().withPlayCount(58))
                                .addSongStatistics(snapshot3, new SongStatistics().withPlayCount(80)))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Arctic Monkeys")
                                .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                                .withSongName("Mardy Bum")
                                .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(61))
                                .addSongStatistics(snapshot2, new SongStatistics().withPlayCount(62))
                                .addSongStatistics(snapshot3, new SongStatistics().withPlayCount(63)))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Nada Surf")
                                .withAlbumName("The Weight is a Gift")
                                .withSongName("Blankest Year")
                                .addSongStatistics(snapshot1, new SongStatistics().withPlayCount(56))
                                .addSongStatistics(snapshot2, new SongStatistics().withPlayCount(59))
                                .addSongStatistics(snapshot3, new SongStatistics().withPlayCount(59))),
                snapshotService.compareSnapshots(asList(snapshot1, snapshot2, snapshot3)));
        //@formatter:on
    }

    /**
     * Test that the order returned in the history is proper, no matter the order given as input.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSnapshotOrdering() throws StatisticsException {
        String earliestSnapshot = "111201 - Music.txt";
        String latestSnapshot = "111205 - Music.txt";
        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .addSnapshot(earliestSnapshot)
                        .addSnapshot(latestSnapshot)
                        .addSongHistory(new SongHistory()
                                .withArtistName("Arctic Monkeys")
                                .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                                .withSongName("Mardy Bum")
                                .addSongStatistics(earliestSnapshot, new SongStatistics().withPlayCount(61))
                                .addSongStatistics(latestSnapshot, new SongStatistics().withPlayCount(62)))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Nada Surf")
                                .withAlbumName("The Weight is a Gift")
                                .withSongName("Blankest Year")
                                .addSongStatistics(earliestSnapshot, new SongStatistics().withPlayCount(56))
                                .addSongStatistics(latestSnapshot, new SongStatistics().withPlayCount(59)))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Death From Above 1979")
                                .withAlbumName("You're A Woman I'm A Machine")
                                .withSongName("Going Steady")
                                .addSongStatistics(earliestSnapshot, new SongStatistics().withPlayCount(55))
                                .addSongStatistics(latestSnapshot, new SongStatistics().withPlayCount(58))),
                snapshotService.compareSnapshots(asList(latestSnapshot, earliestSnapshot)));
        //@formatter:on
    }
}
