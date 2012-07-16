package com.dubious.itunes.statistics.service.test;

import static com.dubious.itunes.statistics.service.SongService.VARIOUS_ALBUM_ARTIST_NAME;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.service.AlbumGrouping;
import com.dubious.itunes.statistics.service.SongFilter;
import com.dubious.itunes.statistics.service.SongService;
import com.dubious.itunes.statistics.service.SongServiceImpl;
import com.dubious.itunes.statistics.store.SongStore;

/**
 * Test interactions of {@link SongService#getSongsForAlbum(String, String)}.
 */
public class SongServiceGetSongsForAlbumInteractionTest {

    private SongStore songStore;
    private SongFilter songFilter;
    private SongService songService;

    /**
     * Test setup.
     */
    @Before
    public final void before() {
        songStore = mock(SongStore.class);
        songFilter = mock(SongFilter.class);
        songService = new SongServiceImpl(mock(AlbumGrouping.class), songFilter, songStore);
    }

    /**
     * Test get songs for album.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testGetSongsForAlbum() throws StatisticsException {
        // setup
        // this interaction also acts as verification of proper call to songStore
        when(songStore.getSongsByArtistAndAlbum("artist", "album")).thenReturn(
                asList(
                        new Song()
                                .withArtistName("artist")
                                .withAlbumName("album")
                                .withName("song1")
                                .withTrackNumber(1),
                        new Song()
                                .withArtistName("artist")
                                .withAlbumName("album")
                                .withName("song2")
                                .withTrackNumber(2)));

        assertEquals(
                asList(
                        new Song()
                                .withArtistName("artist")
                                .withAlbumName("album")
                                .withName("song1")
                                .withTrackNumber(1),
                        new Song()
                                .withArtistName("artist")
                                .withAlbumName("album")
                                .withName("song2")
                                .withTrackNumber(2)),
                songService.getSongsForAlbum("artist", "album"));
    }

    /**
     * Test get songs for album in the "Various" artists scenario.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testGetSongsForAlbumForVariousArtists() throws StatisticsException {
        // setup
        // this interaction also acts as verification of proper call to songStore
        when(songStore.getSongsByAlbum("album")).thenReturn(
                asList(new Song()
                        .withArtistName("artist1")
                        .withAlbumName("album1")
                        .withName("song1")
                        .withTrackNumber(1)));
        // this interaction also acts as verification of proper call to songFilter
        when(
                songFilter.filterForVariousAlbum(asList(new Song()
                        .withArtistName("artist1")
                        .withAlbumName("album1")
                        .withName("song1")
                        .withTrackNumber(1)))).thenReturn(
                asList(new Song()
                        .withArtistName("artist2")
                        .withAlbumName("album2")
                        .withName("song2")
                        .withTrackNumber(2)));

        assertEquals(
                asList(new Song()
                        .withArtistName("artist2")
                        .withAlbumName("album2")
                        .withName("song2")
                        .withTrackNumber(2)),
                songService.getSongsForAlbum(VARIOUS_ALBUM_ARTIST_NAME, "album"));
    }
}
