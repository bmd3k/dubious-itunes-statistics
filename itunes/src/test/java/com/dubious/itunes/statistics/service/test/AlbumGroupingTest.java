package com.dubious.itunes.statistics.service.test;

import static com.dubious.itunes.statistics.service.SongService.VARIOUS_ALBUM_ARTIST_NAME;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.dubious.itunes.model.Album;
import com.dubious.itunes.statistics.service.AlbumGrouper;
import com.dubious.itunes.statistics.service.AlbumGrouping;

/**
 * Test of {@link AlbumGrouping}.
 */
public class AlbumGroupingTest {

    private AlbumGrouping grouper;

    /**
     * Test setup.
     */
    @Before
    public final void before() {
        grouper = new AlbumGrouper();
    }

    /**
     * Test handling empty lists of albums.
     */
    @Test
    public final void testNoAlbums() {
        assertEquals(
                Collections.<Album>emptyList(),
                grouper.groupAlbums(Collections.<Album>emptyList()));
    }

    /**
     * Test that (Artist,Album) combinations with fewer than 4 songs are ignored for grouping when
     * there are no other matching album names.
     */
    @Test
    public final void testNoGrouping() {
        assertEquals(
                asList(
                        new Album()
                                .withArtistName("artist1")
                                .withName("album1_1")
                                .withSongCount(1),
                        new Album()
                                .withArtistName("artist1")
                                .withName("album1_2")
                                .withSongCount(1),
                        new Album()
                                .withArtistName("artist2")
                                .withName("album2_1")
                                .withSongCount(1)),
                grouper.groupAlbums(asList(
                        new Album()
                                .withArtistName("artist1")
                                .withName("album1_1")
                                .withSongCount(1),
                        new Album()
                                .withArtistName("artist1")
                                .withName("album1_2")
                                .withSongCount(1),
                        new Album()
                                .withArtistName("artist2")
                                .withName("album2_1")
                                .withSongCount(1))));
    }

    /**
     * Test that (Artist,Album) combinations with fewer than 4 songs are grouped with other albums
     * with same name.
     */
    @Test
    public final void testGroupingSameAlbumName() {
        assertEquals(
                asList(new Album()
                        .withArtistName(VARIOUS_ALBUM_ARTIST_NAME)
                        .withName("album")
                        .withSongCount(6)),
                grouper
                        .groupAlbums(asList(
                                new Album()
                                        .withArtistName("artist1")
                                        .withName("album")
                                        .withSongCount(3),
                                new Album()
                                        .withArtistName("artist2")
                                        .withName("album")
                                        .withSongCount(1),
                                new Album()
                                        .withArtistName("artist3")
                                        .withName("album")
                                        .withSongCount(2))));
    }

    /**
     * Test that the grouping works for the empty album name.
     */
    @Test
    public final void testGroupingEmptyAlbumName() {
        assertEquals(
                asList(new Album()
                        .withArtistName(VARIOUS_ALBUM_ARTIST_NAME)
                        .withName("")
                        .withSongCount(4)),
                grouper.groupAlbums(asList(new Album()
                        .withArtistName("artist1")
                        .withName("")
                        .withSongCount(3), new Album()
                        .withArtistName("artist2")
                        .withName("")
                        .withSongCount(1))));
    }

    /**
     * Test that grouping works for the null album name.
     */
    @Test
    public final void testGroupingNullAlbumName() {
        assertEquals(
                asList(new Album().withArtistName(VARIOUS_ALBUM_ARTIST_NAME).withSongCount(2)),
                grouper.groupAlbums(asList(new Album()
                        .withArtistName("artist1")
                        .withSongCount(1), new Album()
                        .withArtistName("artist2")
                        .withSongCount(1))));
    }

    /**
     * Test that no grouping is made with an album that has 4 or more songs.
     */
    @Test
    public final void testNoGroupingSameAlbumName() {
        assertEquals(
                asList(
                        new Album().withArtistName("artist1").withName("album").withSongCount(1),
                        new Album()
                                .withArtistName("artist2")
                                .withName("album")
                                .withSongCount(AlbumGrouping.GROUP_THRESHOLD)),
                grouper
                        .groupAlbums(asList(
                                new Album()
                                        .withArtistName("artist1")
                                        .withName("album")
                                        .withSongCount(1),
                                new Album()
                                        .withArtistName("artist2")
                                        .withName("album")
                                        .withSongCount(AlbumGrouping.GROUP_THRESHOLD))));
    }

    /**
     * Test mixed scenario with multiple instances of same album name, but some of the albums have
     * fewer than the threshold and others have more.
     */
    @Test
    public final void testMix() {
        assertEquals(
                asList(
                        new Album()
                                .withArtistName("artist2")
                                .withName("album")
                                .withSongCount(AlbumGrouping.GROUP_THRESHOLD + 1),
                        new Album()
                                .withArtistName("Various")
                                .withName("album")
                                .withSongCount(1 + AlbumGrouping.GROUP_THRESHOLD - 1)),
                grouper
                        .groupAlbums(asList(
                                new Album()
                                        .withArtistName("artist1")
                                        .withName("album")
                                        .withSongCount(1),
                                new Album()
                                        .withArtistName("artist2")
                                        .withName("album")
                                        .withSongCount(AlbumGrouping.GROUP_THRESHOLD + 1),
                                new Album()
                                        .withArtistName("artist3")
                                        .withName("album")
                                        .withSongCount(AlbumGrouping.GROUP_THRESHOLD - 1))));
    }
}
