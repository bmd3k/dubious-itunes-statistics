package com.dubious.itunes.statistics.service.test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dubious.itunes.statistics.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.SnapshotService;
import com.dubious.itunes.statistics.service.SnapshotServiceImpl;
import com.dubious.itunes.statistics.store.ReadOnlySnapshotStore;
import com.dubious.itunes.statistics.store.file.FileSnapshotStore;
import com.dubious.itunes.statistics.store.file.FileStoreProperties;

/**
 * Tests of the {@link SnapshotService}.
 */
public class SnapshotServiceTest {

    private static FileStoreProperties fileStoreProperties;
    private static ReadOnlySnapshotStore snapshotFileStore;
    private static SnapshotService snapshotService;

    /**
     * Setup at the class level.
     */
    @BeforeClass
    public static void beforeClass() {
        fileStoreProperties = new FileStoreProperties("test_files", "UTF-16");
        snapshotFileStore = new FileSnapshotStore(fileStoreProperties);
        snapshotService = new SnapshotServiceImpl(snapshotFileStore);
    }

    /**
     * Test comparing two snapshots with same songs in same order.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testBasicHistory() throws StatisticsException {
        String earliestSnapshot = "111201 - Music.txt";
        String latestSnapshot = "111205 - Music.txt";
        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .withEarliestSnapshot(earliestSnapshot)
                        .withLatestSnapshot(latestSnapshot)
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
                snapshotService.compareSnapshots("111201 - Music.txt", "111205 - Music.txt"));
        //@formatter:on
    }

    /**
     * Test comparing two snapshots where the latest snapshot has songs not in the earliest.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testLatestHasUniqueSongs() throws StatisticsException {
        String earliestSnapshot = "101201 - Music.txt";
        String latestSnapshot = "111201 - Music.txt";
        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .withEarliestSnapshot(earliestSnapshot)
                        .withLatestSnapshot(latestSnapshot)
                        .addSongHistory(new SongHistory()
                                .withArtistName("Arctic Monkeys")
                                .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                                .withSongName("Mardy Bum")
                                .addSongStatistics(earliestSnapshot, new SongStatistics().withPlayCount(35))
                                .addSongStatistics(latestSnapshot, new SongStatistics().withPlayCount(61)))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Nada Surf")
                                .withAlbumName("The Weight is a Gift")
                                .withSongName("Blankest Year")
                                .addSongStatistics(earliestSnapshot, null)
                                .addSongStatistics(latestSnapshot, new SongStatistics().withPlayCount(56)))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Death From Above 1979")
                                .withAlbumName("You're A Woman I'm A Machine")
                                .withSongName("Going Steady")
                                .addSongStatistics(earliestSnapshot, new SongStatistics().withPlayCount(30))
                                .addSongStatistics(latestSnapshot, new SongStatistics().withPlayCount(55))),
                snapshotService.compareSnapshots("101201 - Music.txt", "111201 - Music.txt"));
        //@formatter:on
    }

    /**
     * Test comparing two snapshots where order of songs are different.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testDifferentSongOrders() throws StatisticsException {
        String earliestSnapshot = "111205 - Music.txt";
        String latestSnapshot = "111206 - Music.txt";
        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .withEarliestSnapshot(earliestSnapshot)
                        .withLatestSnapshot(latestSnapshot)
                        .addSongHistory(new SongHistory()
                                .withArtistName("Death From Above 1979")
                                .withAlbumName("You're A Woman I'm A Machine")
                                .withSongName("Going Steady")
                                .addSongStatistics(earliestSnapshot, new SongStatistics().withPlayCount(58))
                                .addSongStatistics(latestSnapshot, new SongStatistics().withPlayCount(80)))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Arctic Monkeys")
                                .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                                .withSongName("Mardy Bum")
                                .addSongStatistics(earliestSnapshot, new SongStatistics().withPlayCount(62))
                                .addSongStatistics(latestSnapshot, new SongStatistics().withPlayCount(63)))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Nada Surf")
                                .withAlbumName("The Weight is a Gift")
                                .withSongName("Blankest Year")
                                .addSongStatistics(earliestSnapshot, new SongStatistics().withPlayCount(59))
                                .addSongStatistics(latestSnapshot, new SongStatistics().withPlayCount(59))),
                snapshotService.compareSnapshots("111205 - Music.txt", "111206 - Music.txt"));
        //@formatter:on
    }

    /**
     * Test comparison of two snapshots where the first snapshot parameter actually is the "latest"
     * of the two.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSnapshot1IsLatest() throws StatisticsException {
        String earliestSnapshot = "111201 - Music.txt";
        String latestSnapshot = "111205 - Music.txt";
        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .withEarliestSnapshot(earliestSnapshot)
                        .withLatestSnapshot(latestSnapshot)
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
                snapshotService.compareSnapshots("111205 - Music.txt", "111201 - Music.txt"));
        //@formatter:on
    }
}
