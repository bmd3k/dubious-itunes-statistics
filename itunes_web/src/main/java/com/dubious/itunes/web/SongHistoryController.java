package com.dubious.itunes.web;

import java.util.ArrayList;
import java.util.Arrays;
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
    public static final List<String> QUARTERLY_SNAPSHOT_HISTORY = Arrays.asList(
            "080930 - Music.txt",
            "081218 - Music.txt",
            "090330 - Music.txt",
            "090625 - Music.txt",
            "090930 - Music.txt",
            "091230 - Music.txt",
            "100324 - Music.txt",
            "100628 - Music.txt",
            "100930 - Music.txt",
            "101218 - Music.txt",
            "110330 - Music.txt",
            "110623 - Music.txt",
            "111006 - Music.txt",
            "111228 - Music.txt");

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

        SongHistory songHistory =
                analysisService.enrichSongHistory(historyService.generateSongHistory(
                        artistName,
                        albumName,
                        songName,
                        QUARTERLY_SNAPSHOT_HISTORY));

        List<Integer> songStatistics = new ArrayList<Integer>();
        for (Map.Entry<String, SongStatistics> statistics : songHistory
                .getSongStatistics()
                .entrySet()) {
            songStatistics.add(statistics.getValue().getDifference());
        }
        return songStatistics;
    }

}
