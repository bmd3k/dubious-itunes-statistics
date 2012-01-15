package com.dubious.itunes.statistics.store.file.test;

import static junit.framework.Assert.assertEquals;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.StoreException;

/**
 * Tests of {@link com.dubious.itunes.statistics.store.file.FileSnapshotStore}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/dubious/itunes/test/itunes-test-context.xml" })
public class FileSnapshotStoreTest {

    @Resource(name = "fileSnapshotStore")
    private SnapshotStore fileSnapshotStore;

    /**
     * Test loading a snapshot from file store.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public final void testFileStore() throws StoreException {
        //@formatter:off
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
                fileSnapshotStore.getSnapshot("111201 - Music.txt"));
        //@formatter:on
    }

    /**
     * Test load of snapshot from file where file has "Play Count" column in place of "Plays"
     * column.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public final void testWithPlayCountColumn() throws StoreException {
        //@formatter:off
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
                fileSnapshotStore.getSnapshot("101130 - Music.txt"));
        //@formatter:on
    }

    /**
     * Test load of snapshot from file where a line has empty play count. It represents 0.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public final void testWithEmptyPlayCount() throws StoreException {
        //@formatter:off
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
                fileSnapshotStore.getSnapshot("091130 - Music.txt"));
        //@formatter:on
    }
}
