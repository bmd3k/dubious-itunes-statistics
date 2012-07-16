package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.service.SongFilter;
import com.dubious.itunes.statistics.service.SongFilterImpl;

/**
 * Tests for {@link SongFilterImpl}.
 */
public class SongFilterTest {

    private SongFilter songFilter;

    /**
     * Test setup.
     */
    @Before
    public final void before() {
        songFilter = new SongFilterImpl();
    }

    /**
     * Test no items to filter.
     */
    @Test
    public final void testNoItems() {
        Assert.assertEquals(
                Collections.<Song>emptyList(),
                songFilter.filterForVariousAlbum(Collections.<Song>emptyList()));
    }

    /**
     * Test one non-filtered item.
     */
    @Test
    public final void testOneItem() {
        Assert.assertEquals(
                asList(new Song()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withName("song")
                        .withTrackNumber(1)),
                songFilter.filterForVariousAlbum(asList(new Song()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withName("song")
                        .withTrackNumber(1))));
    }

    /**
     * Test many non-filtered items.
     */
    @Test
    public final void testNothingToFilter() {
        Assert.assertEquals(
                asList(
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album")
                                .withName("song1")
                                .withTrackNumber(1),
                        new Song()
                                .withArtistName("artist2")
                                .withAlbumName("album")
                                .withName("song2")
                                .withTrackNumber(2),
                        new Song()
                                .withArtistName("artist2")
                                .withAlbumName("album")
                                .withName("song3")
                                .withTrackNumber(3),
                        new Song()
                                .withArtistName("artist2")
                                .withAlbumName("album")
                                .withName("song4")
                                .withTrackNumber(4)),
                songFilter.filterForVariousAlbum(asList(
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album")
                                .withName("song1")
                                .withTrackNumber(1),
                        new Song()
                                .withArtistName("artist2")
                                .withAlbumName("album")
                                .withName("song2")
                                .withTrackNumber(2),
                        new Song()
                                .withArtistName("artist2")
                                .withAlbumName("album")
                                .withName("song3")
                                .withTrackNumber(3),
                        new Song()
                                .withArtistName("artist2")
                                .withAlbumName("album")
                                .withName("song4")
                                .withTrackNumber(4))));
    }

    /**
     * Test that (Artist,Album) combinations that appear a number of times above a certain threshold
     * are filtered out.
     */
    @Test
    public final void testFilterAboveThreshold() {
        Assert.assertEquals(
                Collections.<Song>emptyList(),
                songFilter.filterForVariousAlbum(asList(
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album")
                                .withName("song1")
                                .withTrackNumber(1),
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album")
                                .withName("song2")
                                .withTrackNumber(2),
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album")
                                .withName("song3")
                                .withTrackNumber(3),
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album")
                                .withName("song4")
                                .withTrackNumber(4))));
    }

    /**
     * Test filtering when some songs should be filtered and others not.
     */
    @Test
    public final void testMix() {
        Assert.assertEquals(
                asList(
                        new Song()
                                .withArtistName("artist2")
                                .withAlbumName("album")
                                .withName("song3")
                                .withTrackNumber(3),
                        new Song()
                                .withArtistName("artist3")
                                .withAlbumName("album")
                                .withName("song4")
                                .withTrackNumber(4)),
                songFilter.filterForVariousAlbum(asList(
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album")
                                .withName("song1")
                                .withTrackNumber(1),
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album")
                                .withName("song2")
                                .withTrackNumber(2),
                        new Song()
                                .withArtistName("artist2")
                                .withAlbumName("album")
                                .withName("song3")
                                .withTrackNumber(3),
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album")
                                .withName("song5")
                                .withTrackNumber(5),
                        new Song()
                                .withArtistName("artist3")
                                .withAlbumName("album")
                                .withName("song4")
                                .withTrackNumber(4),
                        new Song()
                                .withArtistName("artist1")
                                .withAlbumName("album")
                                .withName("song6")
                                .withTrackNumber(6))));
    }

}
