package com.dubious.itunes.statistics.store.mongodb.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

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

    // TODO: Note that more complex tests of getSongsForAlbum and getAlbums are in the service
    // layer tests. But some of that should be tested here, instead. And we should use mockito
    // at the service layer.

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
                songStore.getSongsForAlbum("artist", "album"));
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
                songStore.getSongsForAlbum("artist", "album"));
    }
}
