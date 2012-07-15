package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dubious.itunes.model.Album;
import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.SnapshotService;
import com.dubious.itunes.statistics.service.SongService;
import com.dubious.itunes.statistics.store.SnapshotStore;

/**
 * Tests of {@link SongService#getAllAlbums()}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/dubious/itunes/test/itunes-test-context.xml" })
public class SongServiceGetAllAlbumsTest {

    @Resource(name = "mongoDbSnapshotStore")
    private SnapshotStore snapshotStore;
    @Resource(name = "snapshotService")
    private SnapshotService snapshotService;
    @Resource(name = "songService")
    private SongService songService;

    /**
     * Write snapshot to the store.
     * 
     * @param snapshotName The name of the snapshot.
     * @param songsInSnapshot The songs in the snapshot.
     * @throws StatisticsException On error.
     */
    private void writeSnapshot(String snapshotName, List<Song> songsInSnapshot)
            throws StatisticsException {
        Snapshot snapshot = new Snapshot().withName(snapshotName).withDate(new DateTime());
        for (Song song : songsInSnapshot) {
            snapshot.addStatistic(song, new SongStatistics().withPlayCount(1));
        }
        snapshotService.writeSnapshot(snapshot);
    }

    /**
     * Test with no albums.
     */
    @Test
    public final void testWithNoAlbums() {
        assertEquals(0, songService.getAllAlbums().size());
    }

    /**
     * Test with a single song.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testWithOneSnapshotOneArtistOneAlbumOneSong() throws StatisticsException {
        writeSnapshot(
                "snapshot",
                asList(new Song()
                        .withArtistName("artist1")
                        .withAlbumName("album1")
                        .withName("song1")));

        assertEquals(asList(new Album()
                .withArtistName("artist1")
                .withName("album1")
                .withSongCount(1)), songService.getAllAlbums());
    }

    /**
     * Test with multiple songs from the same snapshot,artist,album.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testWithOneSnapshotOneArtistOneAlbumMultipleSongs()
            throws StatisticsException {
        writeSnapshot(
                "snapshot",
                asList(
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album1")
                                .withName("song1"),
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album1")
                                .withName("song2")));

        assertEquals(asList(new Album()
                .withArtistName("artist1")
                .withName("album1")
                .withSongCount(2)), songService.getAllAlbums());
    }

    /**
     * Test with multiple albums from same snapshot,artist.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testWithOneSnapshotOneArtistMultipleAlbums() throws StatisticsException {
        writeSnapshot(
                "snapshot",
                asList(
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album1")
                                .withName("song1_1"),
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album2")
                                .withName("song2_1")));

        assertEquals(
                asList(
                        new Album()
                                .withArtistName("artist1")
                                .withName("album1")
                                .withSongCount(1),
                        new Album()
                                .withArtistName("artist1")
                                .withName("album2")
                                .withSongCount(1)),
                songService.getAllAlbums());
    }

    /**
     * Test with multiple artists in one snapshot.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testWithOneSnapshotMultipleArtists() throws StatisticsException {
        writeSnapshot(
                "snapshot",
                asList(
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album1")
                                .withName("song1_1"),
                        new Song()
                                .withArtistName("artist2")
                                .withAlbumName("album1")
                                .withName("song2_1")));

        assertEquals(
                asList(
                        new Album()
                                .withArtistName("artist1")
                                .withName("album1")
                                .withSongCount(1),
                        new Album()
                                .withArtistName("artist2")
                                .withName("album1")
                                .withSongCount(1)),
                songService.getAllAlbums());
    }

    /**
     * Test with same album over multiple snapshots.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testWithMultipleSnapshotsOneArtistOneAlbum() throws StatisticsException {
        writeSnapshot(
                "snapshot1",
                asList(
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album1")
                                .withName("song1_1"),
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album1")
                                .withName("song1_2"),
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album1")
                                .withName("song1_3")));
        writeSnapshot(
                "snapshot2",
                asList(
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album1")
                                .withName("song1_1"),
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album1")
                                .withName("song1_2"),
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album1")
                                .withName("song1_4")));
        assertEquals(asList(new Album()
                .withArtistName("artist1")
                .withName("album1")
                .withSongCount(4)), songService.getAllAlbums());
    }

    /**
     * Complex multi-snapshot scenario. Also tests the alphabetical sorting of the list.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testWithMultipleSnapshotsMultipleArtists() throws StatisticsException {
        writeSnapshot(
                "snapshot1",
                asList(
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album1")
                                .withName("song1_1_1"),
                        new Song()
                                .withArtistName("b-artist2")
                                .withAlbumName("album1")
                                .withName("song1_1_1")));
        writeSnapshot(
                "snapshot2",
                asList(
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album1")
                                .withName("song1_1_1"),
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album2")
                                .withName("song1_2_1"),
                        new Song()
                                .withArtistName("artist3")
                                .withAlbumName("album1")
                                .withName("song3_1_1")));

        assertEquals(
                asList(
                        new Album()
                                .withArtistName("artist1")
                                .withName("album1")
                                .withSongCount(1),
                        new Album()
                                .withArtistName("artist1")
                                .withName("album2")
                                .withSongCount(1),
                        new Album()
                                .withArtistName("artist3")
                                .withName("album1")
                                .withSongCount(1),
                        new Album()
                                .withArtistName("b-artist2")
                                .withName("album1")
                                .withSongCount(1)),
                songService.getAllAlbums());
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
