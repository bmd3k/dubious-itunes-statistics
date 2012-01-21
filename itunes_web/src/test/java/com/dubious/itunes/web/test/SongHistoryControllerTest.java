package com.dubious.itunes.web.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.AnalysisService;
import com.dubious.itunes.statistics.service.HistoryService;
import com.dubious.itunes.web.SongHistoryController;

/**
 * Tests for {@link SongHistoryController}.
 */
public class SongHistoryControllerTest {

    /**
     * Test.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testGetSongHistory() throws StatisticsException {
        HistoryService historyService = mock(HistoryService.class);
        AnalysisService analysisService = mock(AnalysisService.class);
        SongHistoryController songHistoryController =
                new SongHistoryController(historyService, analysisService);

        SongHistory fromHistory =
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistics("snapshot1", new SongStatistics().withPlayCount(4))
                        .addSongStatistics("snapshot2", new SongStatistics().withPlayCount(6));
        SongHistory fromAnalysis =
                new SongHistory()
                        .withArtistName("artist")
                        .withAlbumName("album")
                        .withSongName("song")
                        .addSongStatistics(
                                "snapshot1",
                                new SongStatistics().withPlayCount(4).withDifference(4))
                        .addSongStatistics(
                                "snapshot2",
                                new SongStatistics().withPlayCount(6).withDifference(2));
        when(
                historyService.generateSongHistory(
                        "artist",
                        "album",
                        "song",
                        SongHistoryController.QUARTERLY_SNAPSHOT_HISTORY)).thenReturn(
                fromHistory);
        when(analysisService.enrichSongHistory(fromHistory)).thenReturn(fromAnalysis);
        assertEquals(
                asList(4, 2),
                songHistoryController.getSongHistory("artist", "album", "song"));
    }
}
