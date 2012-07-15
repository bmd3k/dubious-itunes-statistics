package com.dubious.itunes.statistics.store.file.test;

import static java.util.Arrays.asList;
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

    //@formatter:off
    private Snapshot snapshot111201 = 
            new Snapshot()
                    .withName("111201 - Music.txt")
                    .withDate(new DateTime(2011, 12, 1, 0, 0))
                    .addStatistic(new Song()
                            .withArtistName("Arctic Monkeys")
                            .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                            .withName("Mardy Bum")
                            .withTrackNumber(9),
                            new SongStatistics().withPlayCount(61))
                    .addStatistic(new Song()
                            .withArtistName("Nada Surf")
                            .withAlbumName("The Weight is a Gift")
                            .withName("Blankest Year")
                            .withTrackNumber(7),
                            new SongStatistics().withPlayCount(56))
                    .addStatistic(new Song()
                            .withArtistName("Death From Above 1979")
                            .withAlbumName("You're A Woman I'm A Machine")
                            .withName("Going Steady")
                            .withTrackNumber(3),
                            new SongStatistics().withPlayCount(55));
    private Snapshot snapshot101130 = 
            new Snapshot()
                    .withName("101130 - Music.txt")
                    .withDate(new DateTime(2010, 11, 30, 0, 0))
                    .addStatistic(new Song()
                            .withArtistName("Arctic Monkeys")
                            .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                            .withName("Mardy Bum")
                            .withTrackNumber(9),
                            new SongStatistics().withPlayCount(34))
                    .addStatistic(new Song()
                            .withArtistName("Death From Above 1979")
                            .withAlbumName("You're A Woman I'm A Machine")
                            .withName("Going Steady")
                            .withTrackNumber(3),
                            new SongStatistics().withPlayCount(30));
    private Snapshot snapshot091130 = 
            new Snapshot()
                    .withName("091130 - Music.txt")
                    .withDate(new DateTime(2009, 11, 30, 0, 0))
                    .addStatistic(new Song()
                            .withArtistName("Arctic Monkeys")
                            .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                            .withName("Mardy Bum")
                            .withTrackNumber(9),
                            new SongStatistics().withPlayCount(4))
                    .addStatistic(new Song()
                            .withArtistName("Death From Above 1979")
                            .withAlbumName("You're A Woman I'm A Machine")
                            .withName("Going Steady")
                            .withTrackNumber(3),
                            new SongStatistics().withPlayCount(0));
    
    private Snapshot snapshotNoTrackNumber = 
            new Snapshot()
                    .withName("050505 - Music.txt")
                    .withDate(new DateTime(2005, 05, 05, 0, 0))
                    .addStatistic(new Song()
                            .withArtistName("Arctic Monkeys")
                            .withAlbumName("Whatever People Say I Am, That's What I'm Not")
                            .withName("Mardy Bum"),
                            new SongStatistics().withPlayCount(4));
    //@formatter:on

    /**
     * Test loading a snapshot from file store.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public final void testFileStore() throws StoreException {
        assertEquals(snapshot111201, fileSnapshotStore.getSnapshot(snapshot111201.getName()));
    }

    /**
     * Test load of snapshot from file where file has "Play Count" column in place of "Plays"
     * column.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public final void testWithPlayCountColumn() throws StoreException {
        assertEquals(snapshot101130, fileSnapshotStore.getSnapshot(snapshot101130.getName()));
    }

    /**
     * Test load of snapshot from file where a line has empty play count. It represents 0.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public final void testWithEmptyPlayCount() throws StoreException {
        assertEquals(snapshot091130, fileSnapshotStore.getSnapshot(snapshot091130.getName()));
    }

    /**
     * Test load of snapshot from file where a line has empty track number. It represents null.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public final void testWithEmptyTrackNumber() throws StoreException {
        assertEquals(
                snapshotNoTrackNumber,
                fileSnapshotStore.getSnapshot(snapshotNoTrackNumber.getName()));
    }

    /**
     * Test retrieval all snapshots in the store.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public final void testGetSnapshots() throws StoreException {
        assertEquals(
                asList(snapshotNoTrackNumber, snapshot091130, snapshot101130, snapshot111201),
                fileSnapshotStore.getSnapshots());
    }

    /**
     * Test retrieval of all snapshots but without statistics.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public final void testGetSnapshotsWithoutStatistics() throws StoreException {
        assertEquals(
                asList(
                        new Snapshot().withName(snapshotNoTrackNumber.getName()).withDate(
                                snapshotNoTrackNumber.getDate()),
                        new Snapshot().withName(snapshot091130.getName()).withDate(
                                snapshot091130.getDate()),
                        new Snapshot().withName(snapshot101130.getName()).withDate(
                                snapshot101130.getDate()),
                        new Snapshot().withName(snapshot111201.getName()).withDate(
                                snapshot111201.getDate())),
                fileSnapshotStore.getSnapshotsWithoutStatistics());
    }
}
