package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.dubious.itunes.model.Album;
import com.dubious.itunes.statistics.service.AlbumGrouping;
import com.dubious.itunes.statistics.service.SongService;
import com.dubious.itunes.statistics.service.SongServiceImpl;
import com.dubious.itunes.statistics.store.SongStore;

/**
 * Tests interactions of the method {@link SongService#getAllAlbums()}.
 */
public class SongServiceGetSongsForAlbumInteractionTest {

    private SongStore songStore;
    private AlbumGrouping albumGrouping;
    private SongService songService;

    /**
     * Setup.
     */
    @Before
    public final void before() {
        songStore = mock(SongStore.class);
        albumGrouping = mock(AlbumGrouping.class);
        songService = new SongServiceImpl(albumGrouping, songStore);
    }

    /**
     * Test interaction with {@link AlbumGrouping} object.
     */
    @Test
    public final void testAlbumGrouping() {
        // setup
        when(songStore.getAllAlbums()).thenReturn(
                asList(new Album().withArtistName("artist").withName("album")));
        // this also helps us verify the interaction with the album grouping object
        when(
                albumGrouping.groupAlbums(asList(new Album().withArtistName("artist").withName(
                        "album")))).thenReturn(
                asList(new Album().withArtistName("modified").withName("modified")));

        // exercise and verify
        assertEquals(
                asList(new Album().withArtistName("modified").withName("modified")),
                songService.getAllAlbums());
    }
}
