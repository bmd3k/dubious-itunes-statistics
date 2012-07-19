package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.AnalysisService;
import com.dubious.itunes.statistics.service.AnalysisService.Order;
import com.dubious.itunes.statistics.service.AnalysisServiceImpl;
import com.dubious.itunes.statistics.service.HistoryService;
import com.dubious.itunes.statistics.service.SongHistoryEnricher;
import com.dubious.itunes.statistics.service.SongHistorySorter;

/**
 * Interaction tests for {@link AnalysisService}.
 */
public class AnalysisServiceInteractionTest {

    private HistoryService historyService;
    private AnalysisService analysisService;

    /**
     * Test setup.
     */
    @Before
    public final void before() {
        historyService = mock(HistoryService.class);
        analysisService =
                new AnalysisServiceImpl(
                        historyService,
                        new SongHistoryEnricher(),
                        new SongHistorySorter());
    }

    //@formatter:off
    private SnapshotsHistory originalSnapshotsHistory = new SnapshotsHistory()
            .addSnapshots(asList("snapshot1", "snapshot2", "snapshot3"))
            .addSongHistory(new SongHistory()
                    .withArtistName("artist")
                    .withAlbumName("album")
                    .withSongName("song1")
                    .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(5))
                    .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(6))
                    .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(13)))
            .addSongHistory(new SongHistory()   
                    .withArtistName("artist")
                    .withAlbumName("album")
                    .withSongName("song2")
                    .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(1))
                    .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(4))
                    .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(10)));
    
    private SongHistory enrichedSongHistory1 = new SongHistory()
        .withArtistName("artist")
        .withAlbumName("album")
        .withSongName("song1")
        .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(5).withDifference(5))
        .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(6).withDifference(1))
        .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(13).withDifference(7));
    
    private SongHistory enrichedSongHistory2 = new SongHistory()   
        .withArtistName("artist")
        .withAlbumName("album")
        .withSongName("song2")
        .addSongStatistic("snapshot1", new SongStatistics().withPlayCount(1).withDifference(1))
        .addSongStatistic("snapshot2", new SongStatistics().withPlayCount(4).withDifference(3))
        .addSongStatistic("snapshot3", new SongStatistics().withPlayCount(10).withDifference(6));
    //@formatter:on

    /**
     * Test interactions for
     * {@link AnalysisService#getEnrichedSnapshotsHistory(java.util.List, Order)}.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testGetEnrichedSnapshotsHistory() throws StatisticsException {
        // setup
        // this interaction also acts as verification that the history service is called
        when(
                historyService.generateSnapshotHistory(asList(
                        "snapshot1",
                        "snapshot2",
                        "snapshot3"))).thenReturn(originalSnapshotsHistory);

        // execute and verify return value
        // the return value also helps us determine that the enrichment and sort were properly
        // called
        assertEquals(
                new SnapshotsHistory()
                        .addSnapshots(asList("snapshot1", "snapshot2", "snapshot3"))
                        .addSongHistory(enrichedSongHistory1)
                        .addSongHistory(enrichedSongHistory2),
                analysisService.getEnrichedSnapshotsHistory(
                        asList("snapshot1", "snapshot2", "snapshot3"),
                        Order.PlayCount));
    }

    /**
     * Test that the proper Order value is passed to the {@link SongHistorySorter}.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testGetEnrichedSnapshotsHistoryOrderByDifference()
            throws StatisticsException {
        // setup
        when(
                historyService.generateSnapshotHistory(asList(
                        "snapshot1",
                        "snapshot2",
                        "snapshot3"))).thenReturn(originalSnapshotsHistory);

        // execute and verify return value
        assertEquals(
                new SnapshotsHistory()
                        .addSnapshots(asList("snapshot1", "snapshot2", "snapshot3"))
                        .addSongHistory(enrichedSongHistory2)
                        .addSongHistory(enrichedSongHistory1),
                analysisService.getEnrichedSnapshotsHistory(
                        asList("snapshot1", "snapshot2", "snapshot3"),
                        Order.Difference));
    }

    /**
     * Test interactions for
     * {@link AnalysisService#getEnrichedSongHistory(String, String, String, java.util.List)}.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testGetEnrichedSongHistory() throws StatisticsException {
        // setup
        when(historyService.generateSongHistory("artist", "album", "song", asList("snapshot1")))
                .thenReturn(
                        new SongHistory()
                                .withArtistName("artist")
                                .withAlbumName("album")
                                .withSongName("song")
                                .addSongStatistic(
                                        "snapshot1",
                                        new SongStatistics().withPlayCount(1)));

        // execute and verify return value. This also verifies that song enrichment has been called.
        assertEquals(
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistic(
                                "snapshot1",
                                new SongStatistics().withPlayCount(1).withDifference(1)),
                analysisService.getEnrichedSongHistory(
                        "artist",
                        "album",
                        "song",
                        asList("snapshot1")));

    }
}
