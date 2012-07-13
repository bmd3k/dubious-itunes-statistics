package com.dubious.itunes.web.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
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

    private SongHistoryController songHistoryController;
    private HistoryService historyService;

    /**
     * Setup.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Before
    public final void setUp() throws StatisticsException {

        historyService = mock(HistoryService.class);
        AnalysisService analysisService = mock(AnalysisService.class);
        songHistoryController = new SongHistoryController(historyService, analysisService);

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

        when(historyService.getQuarterlySnapshots())
                .thenReturn(asList("snapshot1", "snapshot2"));
        when(
                historyService.generateSongHistory(
                        "artist",
                        "album",
                        "song",
                        asList("snapshot1", "snapshot2"))).thenReturn(fromHistory);
        when(analysisService.enrichSongHistory(fromHistory)).thenReturn(fromAnalysis);
    }

    /**
     * Test getting song history.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testGetSongHistory() throws StatisticsException {
        assertEquals(
                asList(4, 2),
                songHistoryController.getSongHistory("artist", "album", "song"));
    }

    /**
     * Test getting empty song history.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testGetEmptyHistory() throws StatisticsException {
        assertEquals(asList(0, 0), songHistoryController.getEmptyHistory());
    }

    /**
     * Test that on multiple calls to GetSongHistory the quarterly snapshot is determined only once.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testGetQuarterlySnapshotsCalledOnce() throws StatisticsException {
        songHistoryController.clearQuarterlySnapshotHistoryInCache();
        songHistoryController.getSongHistory("artist", "album", "song");
        verify(historyService, times(1)).getQuarterlySnapshots();
        songHistoryController.getSongHistory("artist", "album", "song");
        songHistoryController.getEmptyHistory();
        verify(historyService, times(1)).getQuarterlySnapshots();

        songHistoryController.clearQuarterlySnapshotHistoryInCache();
        songHistoryController.getEmptyHistory();
        verify(historyService, times(2)).getQuarterlySnapshots();
        songHistoryController.getSongHistory("artist", "album", "song");
        songHistoryController.getEmptyHistory();
        verify(historyService, times(2)).getQuarterlySnapshots();
    }
}
