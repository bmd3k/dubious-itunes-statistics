package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.AlbumDoesNotExistException;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.SongService;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.StoreException;

/**
 * Tests of {@link SongService#getSongsForAlbum(com.dubious.itunes.model.Album)}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/dubious/itunes/test/itunes-test-context.xml" })
public class SongServiceGetSongsForAlbumTest {

    @Resource(name = "mongoDbSnapshotStore")
    private SnapshotStore snapshotStore;
    @Resource(name = "songService")
    private SongService songService;

    private Song artist1Album1Song1 = new Song()
            .withArtistName("artist1")
            .withAlbumName("album1")
            .withName("song1");
    private Song artist1Album1Song2 = new Song()
            .withArtistName("artist1")
            .withAlbumName("album1")
            .withName("song2");
    private Song artist1Album1Song3 = new Song()
            .withArtistName("artist1")
            .withAlbumName("album1")
            .withName("song3");
    private Song artist1Album2Song1 = new Song()
            .withArtistName("artist1")
            .withAlbumName("album2")
            .withName("song1");
    private Song artist2Album1Song1 = new Song()
            .withArtistName("artist2")
            .withAlbumName("album1")
            .withName("song1");
    private Song artist2Album1Song2 = new Song()
            .withArtistName("artist2")
            .withAlbumName("album1")
            .withName("song2");

    /**
     * Write snapshot to the store.
     * 
     * @param snapshotName The name of the snapshot.
     * @param songsInSnapshot The songs in the snapshot.
     * @throws StoreException On error.
     */
    private void writeSnapshot(String snapshotName, List<Song> songsInSnapshot)
            throws StoreException {
        Snapshot snapshot = new Snapshot().withName(snapshotName).withDate(new DateTime());
        for (Song song : songsInSnapshot) {
            snapshot.addStatistic(song, new SongStatistics().withPlayCount(1));
        }
        snapshotStore.writeSnapshot(snapshot);
    }

    /**
     * Test attempt to get songs for an album that does not exist.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testAlbumDoesNotExist() throws StatisticsException {
        try {
            songService.getSongsForAlbum("artist1", "album1");
            fail("expected exception not thrown");
        } catch (AlbumDoesNotExistException e) {
            assertEquals(e, new AlbumDoesNotExistException("artist1", "album1"));
        }
    }

    /**
     * Test a single song in an album.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testOneSong() throws StatisticsException {
        writeSnapshot("snapshot1", asList(artist1Album1Song1));

        assertEquals(
                asList(artist1Album1Song1),
                songService.getSongsForAlbum("artist1", "album1"));
    }

    /**
     * Test multiple songs for an album.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testSongsFromOneSnapshot() throws StatisticsException {
        writeSnapshot(
                "snapshot1",
                asList(artist1Album1Song1, artist1Album1Song2, artist1Album1Song3));
        assertEquals(
                asList(artist1Album1Song1, artist1Album1Song2, artist1Album1Song3),
                songService.getSongsForAlbum("artist1", "album1"));
    }

    /**
     * Test songs for an album from multiple snapshots, with some overlap and some unique.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testSongsFromMultipleSnapshots() throws StatisticsException {
        writeSnapshot("snapshot1", asList(artist1Album1Song1, artist1Album1Song2));
        writeSnapshot("snapshot2", asList(artist1Album1Song2, artist1Album1Song3));

        assertEquals(
                asList(artist1Album1Song1, artist1Album1Song2, artist1Album1Song3),
                songService.getSongsForAlbum("artist1", "album1"));
    }

    /**
     * Test multiple albums. Also tests ordering.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testMultipleAlbums() throws StatisticsException {
        writeSnapshot(
                "snapshot1",
                asList(
                        artist2Album1Song2,
                        artist1Album1Song1,
                        artist2Album1Song1,
                        artist1Album2Song1,
                        artist1Album1Song3,
                        artist1Album1Song2));

        assertEquals(
                asList(artist1Album1Song1, artist1Album1Song2, artist1Album1Song3),
                songService.getSongsForAlbum("artist1", "album1"));
        assertEquals(
                asList(artist1Album2Song1),
                songService.getSongsForAlbum("artist1", "album2"));
        assertEquals(
                asList(artist2Album1Song1, artist2Album1Song2),
                songService.getSongsForAlbum("artist2", "album1"));
    }

    /**
     * Tear down.
     * 
     * @throws StatisticsException On error.
     */
    @After
    public final void after() throws StatisticsException {
        snapshotStore.deleteAll();
    }
}
