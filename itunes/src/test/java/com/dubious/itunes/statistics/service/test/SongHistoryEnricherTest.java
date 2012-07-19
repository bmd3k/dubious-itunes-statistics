package com.dubious.itunes.statistics.service.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.SongHistoryEnricher;

/**
 * Tests of {@link SongHistoryEnricher}.
 */
public class SongHistoryEnricherTest {

    private SongHistoryEnricher enricher;

    /**
     * Test setup.
     */
    @Before
    public final void before() {
        enricher = new SongHistoryEnricher();
    }

    /**
     * Test enrich with no snapshots.
     */
    @Test
    public final void testEnrichWithNoSnapshots() {
        SongHistory songHistory =
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song");

        enricher.enrichSongHistory(songHistory);
        assertEquals(new SongHistory()
                .withArtistName("artist")
                .withAlbumName("album")
                .withSongName("song"), songHistory);
    }

    /**
     * Test enrich with one snapshot.
     */
    @Test
    public final void testEnrichWithOneSnapshot() {
        SongHistory songHistory =
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistic("snapshot", new SongStatistics().withPlayCount(5));

        enricher.enrichSongHistory(songHistory);

        assertEquals(
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistic(
                                "snapshot",
                                new SongStatistics().withPlayCount(5).withDifference(5)),
                songHistory);
    }

    /**
     * Test enrich with two snapshots.
     */
    @Test
    public final void testEnrichWithTwoSnapshots() {
        SongHistory songHistory =
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(5))
                        .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(8));

        enricher.enrichSongHistory(songHistory);

        assertEquals(
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistic(
                                "snapshot1",
                                new SongStatistics().withPlayCount(5).withDifference(5))
                        .addSongStatistic(
                                "snapshot2",
                                new SongStatistics().withPlayCount(8).withDifference(3)),
                songHistory);
    }

    /**
     * Test enrich with many snapshots.
     */
    @Test
    public final void testEnrichWithMultipleSnapshots() {
        SongHistory songHistory =
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(5))
                        .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(8))
                        .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(8))
                        .addSongStatistic("snapshot4", new SongStatistics().withPlayCount(12));

        enricher.enrichSongHistory(songHistory);

        assertEquals(
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistic(
                                "snapshot1",
                                new SongStatistics().withPlayCount(5).withDifference(5))
                        .addSongStatistic(
                                "snapshot2",
                                new SongStatistics().withPlayCount(8).withDifference(3))
                        .addSongStatistic(
                                "snapshot3",
                                new SongStatistics().withPlayCount(8).withDifference(0))
                        .addSongStatistic(
                                "snapshot4",
                                new SongStatistics().withPlayCount(12).withDifference(4)),
                songHistory);
    }

    /**
     * Test enrich with null statistics and play counts.
     */
    @Test
    public final void testEnrichWithNullPlayCounts() {
        SongHistory songHistory =
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(null))
                        .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(3))
                        .addSongStatistic("snapshot3", null)
                        .addSongStatistic("snapshot4", new SongStatistics().withPlayCount(null));

        enricher.enrichSongHistory(songHistory);

        assertEquals(
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistic(
                                "snapshot1",
                                new SongStatistics().withPlayCount(0).withDifference(0))
                        .addSongStatistic(
                                "snapshot2",
                                new SongStatistics().withPlayCount(3).withDifference(3))
                        .addSongStatistic(
                                "snapshot3",
                                new SongStatistics().withPlayCount(0).withDifference(-3))
                        .addSongStatistic(
                                "snapshot4",
                                new SongStatistics().withPlayCount(0).withDifference(0)),
                songHistory);
    }

}
