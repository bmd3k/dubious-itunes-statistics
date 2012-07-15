package com.dubious.itunes.statistics.service.test;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.SnapshotSynchronizationAdjuster;
import com.dubious.itunes.statistics.service.SnapshotSynchronizationAdjustment;

/**
 * Tests of {@link SnapshotSynchronizationAdjustmentTest}.
 */
public class SnapshotSynchronizationAdjustmentTest {

    private SnapshotSynchronizationAdjustment adjustor;

    private DateTime now = new DateTime();

    /**
     * Test setup.
     */
    @Before
    public final void before() {
        adjustor = new SnapshotSynchronizationAdjuster();
    }

    /**
     * Test that fields generally are properly copied over to the adjusted snapshot.
     */
    @Test
    public final void testNoChanges() {
        assertEquals(
                new Snapshot()
                        .withName("snapshot")
                        .withDate(now)
                        .addStatistic(
                                new Song()
                                        .withArtistName("artist")
                                        .withAlbumName("album")
                                        .withName("song")
                                        .withTrackNumber(111),
                                new SongStatistics().withPlayCount(11))
                        .addStatistic(
                                new Song()
                                        .withArtistName("artist2")
                                        .withAlbumName("album2")
                                        .withName("song2")
                                        .withTrackNumber(222),
                                new SongStatistics().withPlayCount(22)),
                adjustor.adjustSnapshotForSynchronization(new Snapshot()
                        .withName("snapshot")
                        .withDate(now)
                        .addStatistic(
                                new Song()
                                        .withArtistName("artist")
                                        .withAlbumName("album")
                                        .withName("song")
                                        .withTrackNumber(111),
                                new SongStatistics().withPlayCount(11))
                        .addStatistic(
                                new Song()
                                        .withArtistName("artist2")
                                        .withAlbumName("album2")
                                        .withName("song2")
                                        .withTrackNumber(222),
                                new SongStatistics().withPlayCount(22))));
    }

    /**
     * Test that track numbers are not altered for songs with albums.
     */
    @Test
    public final void testKeepTrackNumbersForAlbum() {
        assertEquals(
                new Snapshot().addStatistic(new Song()
                        .withAlbumName("album")
                        .withTrackNumber(15), null),
                adjustor.adjustSnapshotForSynchronization(new Snapshot().addStatistic(new Song()
                        .withAlbumName("album")
                        .withTrackNumber(15), null)));
    }

    /**
     * Test that track numbers are altered for songs with albums.
     */
    @Test
    public final void testRemoveTrackNumbersForNoAlbum() {
        assertEquals(
                new Snapshot()
                        .addStatistic(
                                new Song().withAlbumName("album").withTrackNumber(10),
                                null)
                        .addStatistic(new Song(), null)
                        .addStatistic(new Song().withAlbumName(""), null),
                adjustor.adjustSnapshotForSynchronization(new Snapshot()
                        .addStatistic(
                                new Song().withAlbumName("album").withTrackNumber(10),
                                null)
                        .addStatistic(new Song().withTrackNumber(10), null)
                        .addStatistic(new Song().withAlbumName("").withTrackNumber(10), null)));
    }
}
