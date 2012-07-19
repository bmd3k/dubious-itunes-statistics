package com.dubious.itunes.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.AnalysisService;
import com.dubious.itunes.statistics.service.HistoryService;

/**
 * Controller for retrieving information about song history.
 */
@Controller
public class SongHistoryController {

    @Resource(name = "historyService")
    private HistoryService historyService;
    @Resource(name = "analysisService")
    private AnalysisService analysisService;

    /**
     * Default constructor.
     */
    public SongHistoryController() {
    }

    /**
     * Constructor.
     * 
     * @param historyService {@link HistoryService} to inject.
     * @param analysisService {@link AnalysisService} to inject.
     */
    public SongHistoryController(HistoryService historyService, AnalysisService analysisService) {
        this.historyService = historyService;
        this.analysisService = analysisService;
    }

    /**
     * Describes the set of snapshots that are the "Quarterly View".
     */
    private static List<String> quarterlySnapshotHistory = null;

    /**
     * Retrieve the differences in play counts of the song throughout its history.
     * 
     * @param artistName Artist name.
     * @param albumName Album name.
     * @param songName Song name.
     * @return The differences in play counts of the song throughout its history.
     * @throws StatisticsException On error.
     */
    @RequestMapping(value = "/song/getSongHistory.do", method = RequestMethod.GET)
    @ResponseBody
    public final List<Integer> getSongHistory(
            @RequestParam String artistName,
            @RequestParam String albumName,
            @RequestParam String songName) throws StatisticsException {

        if (quarterlySnapshotHistory == null) {
            quarterlySnapshotHistory = historyService.getQuarterlySnapshots();
        }

        SongHistory songHistory =
                analysisService.getEnrichedSongHistory(
                        artistName,
                        albumName,
                        songName,
                        quarterlySnapshotHistory);

        List<Integer> songStatistics = new ArrayList<Integer>();
        for (Map.Entry<String, SongStatistics> statistics : songHistory
                .getSongStatistics()
                .entrySet()) {
            songStatistics.add(statistics.getValue().getDifference());
        }
        return songStatistics;
    }

    /**
     * Retrieve a zeroed data set of song history. It returns a number of elements equal to the
     * number of quarters expected for song history.
     * 
     * @return Zeroed data set.
     * @throws StatisticsException On error.
     */
    @RequestMapping(value = "/song/getEmptyHistory.do", method = RequestMethod.GET)
    @ResponseBody
    public final List<Integer> getEmptyHistory() throws StatisticsException {

        if (quarterlySnapshotHistory == null) {
            quarterlySnapshotHistory = historyService.getQuarterlySnapshots();
        }

        List<Integer> emptyStatistics = new ArrayList<Integer>(quarterlySnapshotHistory.size());
        for (int i = 0; i < quarterlySnapshotHistory.size(); i++) {
            emptyStatistics.add(0);
        }

        return emptyStatistics;
    }

    /**
     * Clear the cached view of quarterly snapshots.
     */
    public final void clearQuarterlySnapshotHistoryInCache() {
        quarterlySnapshotHistory = null;
    }

}
