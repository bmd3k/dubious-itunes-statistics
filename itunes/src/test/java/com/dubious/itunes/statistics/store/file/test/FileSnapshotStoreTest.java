package com.dubious.itunes.statistics.store.file.test;

import static junit.framework.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.StoreException;
import com.dubious.itunes.statistics.store.file.FileSnapshotStore;
import com.dubious.itunes.statistics.store.file.FileStoreProperties;

/**
 * Tests of {@link FileSnapshotStore}.
 */
public class FileSnapshotStoreTest {

    private static FileStoreProperties fileStoreProperties;
    private static SnapshotStore snapshotFileStore;

    /**
     * Setup at the class level.
     */
    @BeforeClass
    public static void beforeClass() {
        fileStoreProperties = new FileStoreProperties("test_files", "UTF-16");
        snapshotFileStore = new FileSnapshotStore(fileStoreProperties);
    }

    /**
     * Test loading a snapshot from file store.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public final void testFileStore() throws StoreException {
        assertEquals(new Snapshot()
                .withName("111201 - Music.txt")
                .withDate(new DateTime(2011, 12, 1, 0, 0))
                .addStatistic(new Song()
                        .withArtistName("Arctic Monkeys")
                        .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                        .withName("Mardy Bum"),
                        new SongStatistics().withPlayCount(61))
                .addStatistic(new Song()
                        .withArtistName("Nada Surf")
                        .withAlbumName("The Weight is a Gift")
                        .withName("Blankest Year"),
                        new SongStatistics().withPlayCount(56))
                .addStatistic(new Song()
                        .withArtistName("Death From Above 1979")
                        .withAlbumName("You're A Woman I'm A Machine")
                        .withName("Going Steady"),
                        new SongStatistics().withPlayCount(55)),
                snapshotFileStore.getSnapshot("111201 - Music.txt"));
    }

    /**
     * Test load of snapshot from file where file has "Play Count" column in place of "Plays"
     * column.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public final void testWithPlayCountColumn() throws StoreException {
        assertEquals(new Snapshot()
                .withName("101130 - Music.txt")
                .withDate(new DateTime(2010, 11, 30, 0, 0))
                .addStatistic(new Song()
                        .withArtistName("Arctic Monkeys")
                        .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                        .withName("Mardy Bum"),
                        new SongStatistics().withPlayCount(34))
                .addStatistic(new Song()
                        .withArtistName("Death From Above 1979")
                        .withAlbumName("You're A Woman I'm A Machine")
                        .withName("Going Steady"),
                        new SongStatistics().withPlayCount(30)),
                snapshotFileStore.getSnapshot("101130 - Music.txt"));
    }

    /**
     * Test load of snapshot from file where a line has empty play count. It represents 0.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public final void testWithEmptyPlayCount() throws StoreException {
        assertEquals(new Snapshot()
                .withName("091130 - Music.txt")
                .withDate(new DateTime(2009, 11, 30, 0, 0))
                .addStatistic(new Song()
                        .withArtistName("Arctic Monkeys")
                        .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                        .withName("Mardy Bum"),
                        new SongStatistics().withPlayCount(4))
                .addStatistic(new Song()
                        .withArtistName("Death From Above 1979")
                        .withAlbumName("You're A Woman I'm A Machine")
                        .withName("Going Steady"),
                        new SongStatistics().withPlayCount(0)),
                snapshotFileStore.getSnapshot("091130 - Music.txt"));
    }
}
