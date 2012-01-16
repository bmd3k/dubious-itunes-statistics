package com.dubious.itunes.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.HistoryService;

@Controller
public class GetSongStatisticsController {

    @Resource(name = "historyService")
    private HistoryService historyService;

    private static final List<String> QUARTERLY_SNAPSHOT_HISTORY = Arrays.asList(
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

    @RequestMapping(value = "/song/getSongStatistics.do", method = RequestMethod.GET)
    public @ResponseBody
    List<Integer> getSongStatistics() throws StatisticsException {

        // TODO: This functionality needs to be moved into the core service layer and tested
        SongHistory songHistory =
                historyService.generateSongHistory(
                        "Radiohead",
                        "Kid A",
                        "Morning Bell",
                        QUARTERLY_SNAPSHOT_HISTORY);

        List<Integer> allStatistics =
                new ArrayList<Integer>(songHistory.getSongStatistics().size());
        for (int i = 0; i < QUARTERLY_SNAPSHOT_HISTORY.size(); i++) {
            SongStatistics statistics =
                    songHistory.getSongStatistics().get(QUARTERLY_SNAPSHOT_HISTORY.get(i));
            if (i == 0) {
                if (statistics == null) {
                    allStatistics.add(0);
                } else {
                    allStatistics.add(statistics.getPlayCount());
                }
            } else {
                SongStatistics lastStatistics =
                        songHistory.getSongStatistics().get(
                                QUARTERLY_SNAPSHOT_HISTORY.get(i - 1));
                if (statistics == null) {
                    allStatistics.add(0);
                } else if (lastStatistics == null) {
                    allStatistics.add(statistics.getPlayCount());
                } else {
                    allStatistics.add(statistics.getPlayCount() - lastStatistics.getPlayCount());
                }
            }
        }

        return allStatistics;
    }

}
