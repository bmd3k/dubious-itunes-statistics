package com.dubious.itunes.statistics.store.file;

import static junit.framework.Assert.assertEquals;

import java.util.Map;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.StoreException;

public class SnapshotFileStoreTest {

    private static FileStoreProperties fileStoreProperties;
    private static SnapshotStore snapshotFileStore;

    @BeforeClass
    public static void beforeClass() {
        fileStoreProperties = new FileStoreProperties("test_files", "UTF-16");
        snapshotFileStore = new SnapshotFileStore(fileStoreProperties);
    }

    @Test
    public void testFileStore() throws StoreException {
        Snapshot snapshot = snapshotFileStore.getSnapshot("111201 - Music.txt");

        assertEquals("111201 - Music.txt", snapshot.getName());
        assertEquals(new DateTime(2011, 12, 1, 0, 0), snapshot.getSnapshotDate());
        Map<Song, SongStatistics> statistics = snapshot.getStatistics();
        assertEquals(3, statistics.size());
        Assert.assertEquals(new SongStatistics().withPlayCount(61),
                statistics.get(new Song()
                        .withArtistName("Arctic Monkeys")
                        .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                        .withName("Mardy Bum")));
        Assert.assertEquals(new SongStatistics().withPlayCount(56),
                statistics.get(new Song()
                        .withArtistName("Nada Surf")
                        .withAlbumName("The Weight is a Gift")
                        .withName("Blankest Year")));
        Assert.assertEquals(new SongStatistics().withPlayCount(55),
                statistics.get(new Song()
                        .withArtistName("Death From Above 1979")
                        .withAlbumName("You're A Woman I'm A Machine")
                        .withName("Going Steady")));
    }
}
