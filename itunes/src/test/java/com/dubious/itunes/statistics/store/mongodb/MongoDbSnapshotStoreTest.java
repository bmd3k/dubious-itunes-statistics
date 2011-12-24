package com.dubious.itunes.statistics.store.mongodb;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.StoreException;
import com.mongodb.Mongo;

/**
 * Tests for MongoDb snapshot store.
 */
public class MongoDbSnapshotStoreTest {

    private static SnapshotStore snapshotStore;

    private DateTime yesterday = new DateMidnight().minusDays(1).toDateTime();

    /**
     * Class-level setup.
     */
    @BeforeClass
    public static void beforeClass() throws UnknownHostException {
        snapshotStore = new MongoDbSnapshotStore(new Mongo().getDB("someDb"));
    }

    /**
     * Tear down tests.
     * 
     * @throws StoreException On unexpected error.
     */
    @After
    public void after() throws StoreException {
        snapshotStore.deleteAll();
    }

    /**
     * Test write to and get from the store.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public void testWriteAndGetBasic() throws StoreException {
        Snapshot snapshot = new Snapshot().withName("My Snapshot");
        snapshotStore.writeSnapshot(snapshot);

        Snapshot snapshotGet = snapshotStore.getSnapshot("My Snapshot");
        assertEquals(snapshot, snapshotGet);
    }

    /**
     * Test attempts to get a snapshot that does not exist.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public void testGetNonExisting() throws StoreException {
        assertNull(snapshotStore.getSnapshot("Does Not Exist"));
    }

    /**
     * Test write and get with dates.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public void testWriteAndGetWithDate() throws StoreException {
        Snapshot snapshot = new Snapshot().withName("My Snapshot").withDate(yesterday);
        snapshotStore.writeSnapshot(snapshot);

        Snapshot snapshotGet = snapshotStore.getSnapshot("My Snapshot");
        assertEquals(snapshot, snapshotGet);
    }

    /**
     * Test write and get with statistics data for a song.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public void testWriteAndGetWithSongStatistic() throws StoreException {
        Snapshot snapshot =
                new Snapshot()
                        .withName("A Snapshot")
                        .withDate(new DateTime())
                        .addStatistic(new Song()
                                .withArtistName("Artist")
                                .withAlbumName("Album")
                                .withName("Song"),
                                new SongStatistics().withPlayCount(12));
        snapshotStore.writeSnapshot(snapshot);

        Snapshot snapshotGet = snapshotStore.getSnapshot("A Snapshot");
        assertEquals(snapshot, snapshotGet);
    }

    /**
     * Test write and get with statistics for multiple songs.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public void testWriteAndGetWithSongStatistics() throws StoreException {
        Snapshot snapshot =
                new Snapshot()
                        .withName("A Snapshot")
                        .withDate(new DateTime())
                        .addStatistic(new Song()
                                .withArtistName("Artist")
                                .withAlbumName("Album")
                                .withName("Song"),
                                new SongStatistics().withPlayCount(12))
                        .addStatistic(new Song()
                                .withArtistName("Artist2")
                                .withAlbumName("Album2")
                                .withName("Song2"),
                                new SongStatistics().withPlayCount(1))
                        .addStatistic(new Song()
                                .withArtistName("Artist3")
                                .withAlbumName("Album3")
                                .withName("Song3"),
                                new SongStatistics().withPlayCount(500));
        snapshotStore.writeSnapshot(snapshot);

        Snapshot snapshotGet = snapshotStore.getSnapshot("A Snapshot");
        assertEquals(snapshot, snapshotGet);
    }

    /**
     * Test attempt to write data for the same snapshot multiple times. The second attempt is
     * ignored.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public void testWriteSnapshotMultipleTimes() throws StoreException {
        Snapshot snapshot1 =
                new Snapshot()
                        .withName("Snapshot")
                        .withDate(new DateTime())
                        .addStatistic(new Song()
                                .withArtistName("Artist")
                                .withAlbumName("Album")
                                .withName("Song"),
                                new SongStatistics().withPlayCount(12));
        snapshotStore.writeSnapshot(snapshot1);

        Snapshot snapshotGet1 = snapshotStore.getSnapshot("Snapshot");
        assertEquals(snapshot1, snapshotGet1);

        Snapshot snapshot2 =
                new Snapshot()
                        .withName("Snapshot")
                        .withDate(yesterday)
                        .addStatistic(new Song()
                                .withArtistName("Artist Updated")
                                .withAlbumName("Album Updated")
                                .withName("Song Updated"),
                                new SongStatistics().withPlayCount(5))
                        .addStatistic(new Song()
                                .withArtistName("Artist")
                                .withAlbumName("Album")
                                .withName("Song"),
                                new SongStatistics().withPlayCount(13));
        snapshotStore.writeSnapshot(snapshot2);

        // attempts to overwrite fail quietly
        Snapshot snapshotGet2 = snapshotStore.getSnapshot("Snapshot");
        assertEquals(snapshot1, snapshotGet2);
    }

    /**
     * Test write and get with multiple snapshots.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public void testWritesAndGets() throws StoreException {
        Snapshot snapshot1 =
                new Snapshot()
                        .withName("Snapshot1")
                        .withDate(new DateTime())
                        .addStatistic(new Song()
                                .withArtistName("Artist")
                                .withAlbumName("Album")
                                .withName("Song"),
                                new SongStatistics().withPlayCount(12));
        snapshotStore.writeSnapshot(snapshot1);
        Snapshot snapshot2 =
                new Snapshot()
                        .withName("Snapshot2")
                        .withDate(yesterday)
                        .addStatistic(new Song()
                                .withArtistName("Artist1")
                                .withAlbumName("Album1")
                                .withName("Song1"),
                                new SongStatistics().withPlayCount(1))
                        .addStatistic(new Song()
                                .withArtistName("Artist2")
                                .withAlbumName("Album2")
                                .withName("Song2"),
                                new SongStatistics().withPlayCount(2));
        snapshotStore.writeSnapshot(snapshot2);
        Snapshot snapshot3 =
                new Snapshot()
                        .withName("Snapshot3")
                        .withDate(yesterday)
                        .addStatistic(new Song()
                                .withArtistName("Artist1")
                                .withAlbumName("Album1")
                                .withName("Song2"),
                                new SongStatistics().withPlayCount(400))
                        .addStatistic(new Song()
                                .withArtistName("Artist3")
                                .withAlbumName("Album3")
                                .withName("Song3"),
                                new SongStatistics().withPlayCount(35));
        snapshotStore.writeSnapshot(snapshot3);

        assertEquals(snapshot1, snapshotStore.getSnapshot("Snapshot1"));
        assertEquals(snapshot2, snapshotStore.getSnapshot("Snapshot2"));
        assertEquals(snapshot3, snapshotStore.getSnapshot("Snapshot3"));
    }

    /**
     * Test deleteAll when there is a single snapshot in the store.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public void testDeleteAllWithOne() throws StoreException {
        String snapshotName = "Snapshot";

        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshotName)
                .withDate(new DateTime())
                .addStatistic(new Song()
                        .withArtistName("Artist")
                        .withAlbumName("Album")
                        .withName("Song"),
                        new SongStatistics().withPlayCount(1)));
        // sanity check
        assertNotNull(snapshotStore.getSnapshot(snapshotName));

        snapshotStore.deleteAll();
        assertNull(snapshotStore.getSnapshot(snapshotName));
    }

    /**
     * Test delete all when there are multiple snapshots in the store.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public void testDeleteAllWithMultiple() throws StoreException {
        String snapshotName1 = "Snapshot";
        String snapshotName2 = "Snapshot2";

        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshotName1)
                .withDate(new DateTime()));
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshotName2)
                .withDate(new DateTime()));
        // sanity check
        assertNotNull(snapshotStore.getSnapshot(snapshotName1));
        assertNotNull(snapshotStore.getSnapshot(snapshotName2));

        snapshotStore.deleteAll();
        assertNull(snapshotStore.getSnapshot(snapshotName1));
        assertNull(snapshotStore.getSnapshot(snapshotName2));
    }

    /**
     * Test that snapshot data can be written after deleteAll.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public void testWriteAfterDelete() throws StoreException {
        String snapshotName = "Snapshot";

        Snapshot snapshot1 = new Snapshot().withName(snapshotName).withDate(new DateTime());
        snapshotStore.writeSnapshot(snapshot1);
        // sanity check
        assertEquals(snapshot1, snapshotStore.getSnapshot(snapshotName));

        // delete
        snapshotStore.deleteAll();

        // readd all
        Snapshot snapshot2 = new Snapshot().withName(snapshotName).withDate(yesterday);
        snapshotStore.writeSnapshot(snapshot2);
        assertEquals(snapshot2, snapshotStore.getSnapshot(snapshotName));
    }
}
