package com.dubious.itunes.statistics.service.test;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.AnalysisService;

/**
 * Tests of {@link AnalysisService#enrichSongHistory}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/dubious/itunes/test/itunes-test-context.xml" })
public class AnalysisServiceEnrichSongHistoryTest {

    @Resource(name = "analysisService")
    private AnalysisService analysisService;

    /**
     * Test enrich with no snapshots.
     */
    @Test
    public final void testEnrichWithNoSnapshots() {
        assertEquals(
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song"),
                analysisService.enrichSongHistory(new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")));
    }

    /**
     * Test enrich with one snapshot.
     */
    @Test
    public final void testEnrichWithOneSnapshot() {
        assertEquals(
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistics(
                                "snapshot",
                                new SongStatistics().withPlayCount(5).withDifference(5)),
                analysisService.enrichSongHistory(new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistics("snapshot", new SongStatistics().withPlayCount(5))));
    }

    /**
     * Test enrich with two snapshots.
     */
    @Test
    public final void testEnrichWithTwoSnapshots() {
        assertEquals(
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistics(
                                "snapshot1",
                                new SongStatistics().withPlayCount(5).withDifference(5))
                        .addSongStatistics(
                                "snapshot2",
                                new SongStatistics().withPlayCount(8).withDifference(3)),
                analysisService.enrichSongHistory(new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistics("snapshot1", new SongStatistics().withPlayCount(5))
                        .addSongStatistics("snapshot2", new SongStatistics().withPlayCount(8))));
    }

    /**
     * Test enrich with many snapshots.
     */
    @Test
    public final void testEnrichWithMultipleSnapshots() {
        assertEquals(
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistics(
                                "snapshot1",
                                new SongStatistics().withPlayCount(5).withDifference(5))
                        .addSongStatistics(
                                "snapshot2",
                                new SongStatistics().withPlayCount(8).withDifference(3))
                        .addSongStatistics(
                                "snapshot3",
                                new SongStatistics().withPlayCount(8).withDifference(0))
                        .addSongStatistics(
                                "snapshot4",
                                new SongStatistics().withPlayCount(12).withDifference(4)),
                analysisService.enrichSongHistory(new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistics("snapshot1", new SongStatistics().withPlayCount(5))
                        .addSongStatistics("snapshot2", new SongStatistics().withPlayCount(8))
                        .addSongStatistics("snapshot3", new SongStatistics().withPlayCount(8))
                        .addSongStatistics("snapshot4", new SongStatistics().withPlayCount(12))));
    }

    /**
     * Test enrich with null statistics and play counts.
     */
    @Test
    public final void testEnrichWithNullPlayCounts() {
        assertEquals(
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistics(
                                "snapshot1",
                                new SongStatistics().withPlayCount(0).withDifference(0))
                        .addSongStatistics(
                                "snapshot2",
                                new SongStatistics().withPlayCount(3).withDifference(3))
                        .addSongStatistics(
                                "snapshot3",
                                new SongStatistics().withPlayCount(0).withDifference(-3))
                        .addSongStatistics(
                                "snapshot4",
                                new SongStatistics().withPlayCount(0).withDifference(0)),
                analysisService.enrichSongHistory(new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistics("snapshot1", new SongStatistics().withPlayCount(null))
                        .addSongStatistics("snapshot2", new SongStatistics().withPlayCount(3))
                        .addSongStatistics("snapshot3", null)
                        .addSongStatistics("snapshot4", new SongStatistics().withPlayCount(null))));
    }

}
