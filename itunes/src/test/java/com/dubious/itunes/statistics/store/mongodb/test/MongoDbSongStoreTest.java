package com.dubious.itunes.statistics.store.mongodb.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.SongStore;

/**
 * Tests for {@link com.dubious.itunes.statistics.store.mongodb.MongoDbSongStore}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/dubious/itunes/test/itunes-test-context.xml" })
public class MongoDbSongStoreTest {

    @Resource(name = "mongoDbSnapshotStore")
    private SnapshotStore snapshotStore;
    @Resource(name = "mongoDbSongStore")
    private SongStore songStore;

    // TODO: Note that more complex tests of getSongsByArtistAndAlbum and getAlbums are in the
    // service layer tests. But some of that should be tested here, instead. And we should use
    // mockito at the service layer.
    // Tests for getSongsByAlbum are properly written following this mock-based approach.

    /**
     * Tear down.
     * 
     * @throws StatisticsException On error.
     */
    @After
    public final void after() throws StatisticsException {
        snapshotStore.deleteAll();
    }

    /**
     * Test basic write and retrieval.
     */
    @Test
    public final void testWriteAndGet() {
        songStore.writeSongs(asList(new Song()
                .withArtistName("artist")
                .withAlbumName("album")
                .withName("song")
                .withTrackNumber(4)));

        assertEquals(
                asList(new Song()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withName("song")
                        .withTrackNumber(4)),
                songStore.getSongsByArtistAndAlbum("artist", "album"));
    }

    /**
     * Test write of same song multiple times.
     */
    @Test
    public final void testWriteMultipleTimesWithDifferentData() {
        songStore.writeSongs(asList(new Song()
                .withArtistName("artist")
                .withAlbumName("album")
                .withName("song")
                .withTrackNumber(4)));
        songStore.writeSongs(asList(new Song()
                .withArtistName("artist")
                .withAlbumName("album")
                .withName("song")
                .withTrackNumber(8)));
        songStore.writeSongs(asList(new Song()
                .withArtistName("artist")
                .withAlbumName("album")
                .withName("song")
                .withTrackNumber(5)));

        assertEquals(
                asList(new Song()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withName("song")
                        .withTrackNumber(5)),
                songStore.getSongsByArtistAndAlbum("artist", "album"));
    }

    /**
     * Test retrieval of songs by album name.
     */
    @Test
    public final void testGetSongsByAlbum() {
        songStore.writeSongs(asList(new Song()
                .withArtistName("artist1")
                .withAlbumName("album")
                .withName("song")
                .withTrackNumber(1)));
        songStore.writeSongs(asList(new Song()
                .withArtistName("artist2")
                .withAlbumName("album")
                .withName("song")
                .withTrackNumber(3)));
        songStore.writeSongs(asList(new Song()
                .withArtistName("artist3")
                .withAlbumName("album")
                .withName("song")
                .withTrackNumber(2)));
        songStore.writeSongs(asList(new Song()
                .withArtistName("artist3")
                .withAlbumName("album")
                .withName("song2")
                .withTrackNumber(4)));

        // with many track numb
        assertEquals(
                asList(
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album")
                                .withName("song")
                                .withTrackNumber(1),
                        new Song()
                                .withArtistName("artist3")
                                .withAlbumName("album")
                                .withName("song")
                                .withTrackNumber(2),
                        new Song()
                                .withArtistName("artist2")
                                .withAlbumName("album")
                                .withName("song")
                                .withTrackNumber(3),
                        new Song()
                                .withArtistName("artist3")
                                .withAlbumName("album")
                                .withName("song2")
                                .withTrackNumber(4)),
                songStore.getSongsByAlbum("album"));
    }

    /**
     * Test retrieval of songs by album name when other albums are present in the store.
     */
    @Test
    public final void testGetSongsByAlbumWithOthers() {
        songStore.writeSongs(asList(new Song()
                .withArtistName("artist1")
                .withAlbumName("album1")
                .withName("song")
                .withTrackNumber(1)));
        songStore.writeSongs(asList(new Song()
                .withArtistName("artist2")
                .withAlbumName("album2")
                .withName("song")
                .withTrackNumber(3)));
        songStore.writeSongs(asList(new Song()
                .withArtistName("artist3")
                .withAlbumName("album1")
                .withName("song")
                .withTrackNumber(2)));

        // with many track numb
        assertEquals(
                asList(
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album1")
                                .withName("song")
                                .withTrackNumber(1),
                        new Song()
                                .withArtistName("artist3")
                                .withAlbumName("album1")
                                .withName("song")
                                .withTrackNumber(2)),
                songStore.getSongsByAlbum("album1"));

    }

    /**
     * Test retrieval of songs by album name when there are no songs matching the criteria.
     */
    @Test
    public final void testGetSongsByAlbumWithNoResult() {
        assertEquals(Collections.<Song>emptyList(), songStore.getSongsByAlbum("album1"));
    }

    /**
     * Test retrieval of songs by album name when album name is null.
     */
    @Test
    public final void testGetSongsByAlbumWithNullName() {
        songStore.writeSongs(asList(new Song()
                .withArtistName("artist")
                .withName("song")
                .withTrackNumber(1)));
        songStore.writeSongs(asList(new Song()
                .withArtistName("artist")
                .withAlbumName("album")
                .withName("song")
                .withTrackNumber(1)));

        assertEquals(asList(new Song()
                .withArtistName("artist")
                .withName("song")
                .withTrackNumber(1)), songStore.getSongsByArtistAndAlbum("artist", null));
    }
}
