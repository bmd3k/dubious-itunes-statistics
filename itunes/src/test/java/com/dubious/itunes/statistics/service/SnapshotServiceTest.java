package com.dubious.itunes.statistics.service;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dubious.itunes.model.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.file.FileStoreProperties;
import com.dubious.itunes.statistics.store.file.SnapshotFileStore;

public class SnapshotServiceTest {

    private static FileStoreProperties fileStoreProperties;
    private static SnapshotStore snapshotFileStore;
    private static SnapshotService analysisService;

    @BeforeClass
    public static void beforeClass() {
        fileStoreProperties = new FileStoreProperties("test_files", "UTF-16");
        snapshotFileStore = new SnapshotFileStore(fileStoreProperties);
        analysisService = new SnapshotServiceImpl(snapshotFileStore);
    }

    @Test
    public void testBasicHistory() throws StatisticsException {
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
                analysisService.compareSnapshots("111201 - Music.txt", "111205 - Music.txt"));
        //@formatter:on
    }

    @Test
    public void testLatestHasUniqueSongs() throws StatisticsException {
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
                analysisService.compareSnapshots("101201 - Music.txt", "111201 - Music.txt"));
        //@formatter:on
    }

    @Test
    public void testDifferentSongOrders() throws StatisticsException {
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
                analysisService.compareSnapshots("111205 - Music.txt", "111206 - Music.txt"));
        //@formatter:on
    }

    @Test
    public void testSnapshot1IsLatest() throws StatisticsException {
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
                analysisService.compareSnapshots("111205 - Music.txt", "111201 - Music.txt"));
        //@formatter:on
    }
}
