package com.dubious.itunes.statistics.service.test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dubious.itunes.statistics.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;
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
        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .withEarliestSnapshot("111201 - Music.txt")
                        .withLatestSnapshot("111205 - Music.txt")
                        .addSongHistory(new SongHistory()
                                .withArtistName("Arctic Monkeys")
                                .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                                .withSongName("Mardy Bum")
                                .withEarliestPlayCount(61)
                                .withLatestPlayCount(62))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Nada Surf")
                                .withAlbumName("The Weight is a Gift")
                                .withSongName("Blankest Year")
                                .withEarliestPlayCount(56)
                                .withLatestPlayCount(59))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Death From Above 1979")
                                .withAlbumName("You're A Woman I'm A Machine")
                                .withSongName("Going Steady")
                                .withEarliestPlayCount(55)
                                .withLatestPlayCount(58)),
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
        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .withEarliestSnapshot("101201 - Music.txt")
                        .withLatestSnapshot("111201 - Music.txt")
                        .addSongHistory(new SongHistory()
                                .withArtistName("Arctic Monkeys")
                                .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                                .withSongName("Mardy Bum")
                                .withEarliestPlayCount(35)
                                .withLatestPlayCount(61))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Nada Surf")
                                .withAlbumName("The Weight is a Gift")
                                .withSongName("Blankest Year")
                                .withLatestPlayCount(56))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Death From Above 1979")
                                .withAlbumName("You're A Woman I'm A Machine")
                                .withSongName("Going Steady")
                                .withEarliestPlayCount(30)
                                .withLatestPlayCount(55)),
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
        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .withEarliestSnapshot("111205 - Music.txt")
                        .withLatestSnapshot("111206 - Music.txt")
                        .addSongHistory(new SongHistory()
                                .withArtistName("Death From Above 1979")
                                .withAlbumName("You're A Woman I'm A Machine")
                                .withSongName("Going Steady")
                                .withEarliestPlayCount(58)
                                .withLatestPlayCount(80))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Arctic Monkeys")
                                .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                                .withSongName("Mardy Bum")
                                .withEarliestPlayCount(62)
                                .withLatestPlayCount(63))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Nada Surf")
                                .withAlbumName("The Weight is a Gift")
                                .withSongName("Blankest Year")
                                .withEarliestPlayCount(59)
                                .withLatestPlayCount(59)),
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
        //@formatter:off
        assertEquals(
                new SnapshotsHistory()
                        .withEarliestSnapshot("111201 - Music.txt")
                        .withLatestSnapshot("111205 - Music.txt")
                        .addSongHistory(new SongHistory()
                                .withArtistName("Arctic Monkeys")
                                .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                                .withSongName("Mardy Bum")
                                .withEarliestPlayCount(61)
                                .withLatestPlayCount(62))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Nada Surf")
                                .withAlbumName("The Weight is a Gift")
                                .withSongName("Blankest Year")
                                .withEarliestPlayCount(56)
                                .withLatestPlayCount(59))
                        .addSongHistory(new SongHistory()
                                .withArtistName("Death From Above 1979")
                                .withAlbumName("You're A Woman I'm A Machine")
                                .withSongName("Going Steady")
                                .withEarliestPlayCount(55)
                                .withLatestPlayCount(58)),
                snapshotService.compareSnapshots("111205 - Music.txt", "111201 - Music.txt"));
        //@formatter:on
    }
}
