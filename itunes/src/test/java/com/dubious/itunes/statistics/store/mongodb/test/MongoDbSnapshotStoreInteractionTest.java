package com.dubious.itunes.statistics.store.mongodb.test;

import static com.dubious.itunes.statistics.store.mongodb.MongoDbSnapshotStore.SNAPSHOT_COLLECTION_NAME;
import static com.dubious.itunes.statistics.store.mongodb.MongoDbSnapshotStore.SONG_STATISTICS_COLLECTION_NAME;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.SongStore;
import com.dubious.itunes.statistics.store.StoreException;
import com.dubious.itunes.statistics.store.mongodb.MongoDbDataSource;
import com.dubious.itunes.statistics.store.mongodb.MongoDbSnapshotStore;
import com.dubious.itunes.statistics.store.mongodb.MongoDbSongStore;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * Use Mockito to test interactions between {@link MongoDbSnapshotStore} and other components to
 * which it delegates work.
 */
public class MongoDbSnapshotStoreInteractionTest {

    private SnapshotStore snapshotStore;
    private SongStore songStore;

    /**
     * Setup the tests.
     * 
     * @throws StoreException On unexpected error.
     */
    @Before
    public final void before() throws StoreException {
        DB db = mock(DB.class);
        MongoDbDataSource dataSource = mock(MongoDbDataSource.class);
        songStore = mock(MongoDbSongStore.class);

        when(db.getCollection(SNAPSHOT_COLLECTION_NAME)).thenReturn(mock(DBCollection.class));
        when(db.getCollection(SONG_STATISTICS_COLLECTION_NAME)).thenReturn(
                mock(DBCollection.class));
        when(dataSource.getDB()).thenReturn(db);
        snapshotStore = new MongoDbSnapshotStore(dataSource, songStore);
    }

    /**
     * Tests that write of song data is delegated to an {@link SongStore}.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public final void testWriteSong() throws StoreException {

        snapshotStore.writeSnapshot(new Snapshot().addStatistic(
                new Song().withArtistName("Artist").withAlbumName("Album").withName("Song"),
                new SongStatistics().withPlayCount(12)));

        Set<Song> songs = new HashSet<Song>();
        songs.add(new Song().withArtistName("Artist").withAlbumName("Album").withName("Song"));
        verify(songStore).writeSongs(songs);
    }

    /**
     * Tests that write of multiple song data is delegated to {@link SongStore}.
     * 
     * @throws StoreException On unexpected error.
     */
    @Test
    public final void testWriteSongs() throws StoreException {
        snapshotStore.writeSnapshot(new Snapshot()
                .addStatistic(
                        new Song()
                                .withArtistName("Artist")
                                .withAlbumName("Album")
                                .withName("Song")
                                .withTrackNumber(1),
                        new SongStatistics().withPlayCount(12))
                .addStatistic(
                        new Song()
                                .withArtistName("Artist")
                                .withAlbumName("Album")
                                .withName("Song1")
                                .withTrackNumber(2),
                        new SongStatistics().withPlayCount(12))
                .addStatistic(
                        new Song()
                                .withArtistName("Artist2")
                                .withAlbumName("Album2")
                                .withName("Song2")
                                .withTrackNumber(3),
                        new SongStatistics().withPlayCount(12)));

        Set<Song> songs = new HashSet<Song>();
        songs.add(new Song()
                .withArtistName("Artist")
                .withAlbumName("Album")
                .withName("Song")
                .withTrackNumber(1));
        songs.add(new Song()
                .withArtistName("Artist")
                .withAlbumName("Album")
                .withName("Song1")
                .withTrackNumber(2));
        songs.add(new Song()
                .withArtistName("Artist2")
                .withAlbumName("Album2")
                .withName("Song2")
                .withTrackNumber(3));
        verify(songStore).writeSongs(songs);
    }
}
