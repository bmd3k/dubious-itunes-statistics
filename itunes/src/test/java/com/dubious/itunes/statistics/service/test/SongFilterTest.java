package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.service.AlbumGrouping;
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
        List<Song> songs = new ArrayList<Song>(AlbumGrouping.GROUP_THRESHOLD);
        List<Song> expectedSongs = new ArrayList<Song>(AlbumGrouping.GROUP_THRESHOLD);
        // have one artist with a single song in the album
        songs.add(new Song()
                .withArtistName("artist1")
                .withAlbumName("album")
                .withName("song1_" + 1)
                .withTrackNumber(1));
        expectedSongs.add(new Song()
                .withArtistName("artist1")
                .withAlbumName("album")
                .withName("song1_" + 1)
                .withTrackNumber(1));
        // have one artist with as many songs as allowed in the same album without being filtered
        for (int i = 1; i <= AlbumGrouping.GROUP_THRESHOLD - 1; i++) {
            songs.add(new Song()
                    .withArtistName("artist2")
                    .withAlbumName("album")
                    .withName("song2_" + i)
                    .withTrackNumber(i));
            expectedSongs.add(new Song()
                    .withArtistName("artist2")
                    .withAlbumName("album")
                    .withName("song2_" + i)
                    .withTrackNumber(i));
        }

        Assert.assertEquals(expectedSongs, songFilter.filterForVariousAlbum(songs));
    }

    /**
     * Test that (Artist,Album) combinations that appear a number of times above a certain threshold
     * are filtered out.
     */
    @Test
    public final void testFilterAboveThreshold() {
        List<Song> songs = new ArrayList<Song>(AlbumGrouping.GROUP_THRESHOLD);
        for (int i = 1; i <= AlbumGrouping.GROUP_THRESHOLD; i++) {
            songs.add(new Song()
                    .withArtistName("artist")
                    .withAlbumName("album")
                    .withName("song" + i)
                    .withTrackNumber(i));
        }

        Assert.assertEquals(
                Collections.<Song>emptyList(),
                songFilter.filterForVariousAlbum(songs));
    }

    /**
     * Test filtering when some songs should be filtered and others not.
     */
    @Test
    public final void testMix() {
        List<Song> songs = new ArrayList<Song>(AlbumGrouping.GROUP_THRESHOLD);
        List<Song> expectedSongs = new ArrayList<Song>(AlbumGrouping.GROUP_THRESHOLD);
        // have one artist with a single song in the album
        songs.add(new Song()
                .withArtistName("artist1")
                .withAlbumName("album")
                .withName("song1_1")
                .withTrackNumber(1));
        expectedSongs.add(new Song()
                .withArtistName("artist1")
                .withAlbumName("album")
                .withName("song1_1")
                .withTrackNumber(1));
        // there is a second artist who will have more than the threshold. put their first and
        // second songs next.
        songs.add(new Song()
                .withArtistName("artist2")
                .withAlbumName("album")
                .withName("song2_1")
                .withTrackNumber(1));
        songs.add(new Song()
                .withArtistName("artist2")
                .withAlbumName("album")
                .withName("song2_2")
                .withTrackNumber(2));
        // there is a 3rd artist, who also will not be filtered
        songs.add(new Song()
                .withArtistName("artist3")
                .withAlbumName("album")
                .withName("song3_1")
                .withTrackNumber(2));
        expectedSongs.add(new Song()
                .withArtistName("artist3")
                .withAlbumName("album")
                .withName("song3_1")
                .withTrackNumber(2));
        // now have remaining songs for the 2nd artist
        for (int i = 3; i <= AlbumGrouping.GROUP_THRESHOLD; i++) {
            songs.add(new Song()
                    .withArtistName("artist2")
                    .withAlbumName("album")
                    .withName("song2_" + i)
                    .withTrackNumber(i));
        }

        Assert.assertEquals(expectedSongs, songFilter.filterForVariousAlbum(songs));
    }

}
