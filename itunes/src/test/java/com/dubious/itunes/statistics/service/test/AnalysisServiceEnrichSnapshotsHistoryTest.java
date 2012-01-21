package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.AnalysisService;

/**
 * Tests of {@link AnalysisService#enrichSnapshotsHistory}.
 * 
 * NOTE: Any SongHistory-specific logic is tested in {@link AnalysisServiceEnrichSongHistoryTest}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/dubious/itunes/test/itunes-test-context.xml" })
public class AnalysisServiceEnrichSnapshotsHistoryTest {

    // TODO: Should have an integration test with HistoryService

    @Resource(name = "analysisService")
    private AnalysisService analysisService;

    /**
     * Test enrich with many snapshots.
     */
    @Test
    public final void testEnrich() {
        // @formatter:off
        assertEquals(new SnapshotsHistory()
                .addSnapshots(asList("snapshot1", "snapshot2", "snapshot3"))
                .addSongHistory(new SongHistory()
                        .withArtistName("artist1")
                        .withAlbumName("album1")
                        .withSongName("song1")
                        .addSongStatistics(
                                "snapshot1",
                                new SongStatistics().withPlayCount(2).withDifference(2))
                        .addSongStatistics(
                                "snapshot2",
                                new SongStatistics().withPlayCount(6).withDifference(4))
                        .addSongStatistics(
                                "snapshot3",
                                new SongStatistics().withPlayCount(9).withDifference(3)))
                .addSongHistory(new SongHistory()
                        .withArtistName("artist2")
                        .withAlbumName("album2")
                        .withSongName("song2")
                        .addSongStatistics(
                                "snapshot1",
                                new SongStatistics().withPlayCount(5).withDifference(5))
                        .addSongStatistics(
                                "snapshot2",
                                new SongStatistics().withPlayCount(7).withDifference(2))
                        .addSongStatistics(
                                "snapshot3",
                                new SongStatistics().withPlayCount(8).withDifference(1))),
            analysisService.enrichSnapshotsHistory(new SnapshotsHistory()
                .addSnapshots(asList("snapshot1", "snapshot2", "snapshot3"))
                .addSongHistory(new SongHistory()
                        .withArtistName("artist1")
                        .withAlbumName("album1")
                        .withSongName("song1")
                        .addSongStatistics("snapshot1", new SongStatistics().withPlayCount(2))
                        .addSongStatistics("snapshot2", new SongStatistics().withPlayCount(6))
                        .addSongStatistics("snapshot3", new SongStatistics().withPlayCount(9)))
                .addSongHistory(new SongHistory()
                        .withArtistName("artist2")
                        .withAlbumName("album2")
                        .withSongName("song2")
                        .addSongStatistics("snapshot1", new SongStatistics().withPlayCount(5))
                        .addSongStatistics("snapshot2", new SongStatistics().withPlayCount(7))
                        .addSongStatistics("snapshot3", new SongStatistics().withPlayCount(8)))));
        //@formatter:on
    }
}
