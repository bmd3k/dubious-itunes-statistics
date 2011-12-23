package com.dubious.itunes.statistics.service;

import static org.apache.commons.io.FileUtils.writeLines;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.dubious.itunes.model.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;

public class AnalysisServiceImpl implements AnalysisService {

    @Override
    public void writeAnalysis(SnapshotsHistory history, String outputPath)
            throws StatisticsException {
        writeAnalysis(history.getSongHistories(), outputPath);
    }

    @Override
    public void writeAnalysisOrderByDifference(SnapshotsHistory history, String outputPath)
            throws StatisticsException {
        // need to make a copy of the list to be sorted as the one passed to us is unmodifiable
        List<SongHistory> sortedSongHistories =
                new ArrayList<SongHistory>(history.getSongHistories());
        Collections.sort(sortedSongHistories, new Comparator<SongHistory>() {

            @Override
            public int compare(SongHistory history1, SongHistory history2) {
                return calculatePlayDifference(history2) - calculatePlayDifference(history1);
            }
        });
        writeAnalysis(sortedSongHistories, outputPath);
    }

    private void writeAnalysis(List<SongHistory> songHistories, String outputPath)
            throws StatisticsException {
        try {
            writeLines(new File(outputPath), "UTF-8", getDataToWrite(songHistories), false);
        } catch (Throwable t) {
            throw new StatisticsException("Unexpected error: ", t);
        }
    }

    private List<String> getDataToWrite(List<SongHistory> songHistories) throws IOException {
        List<String> lines = new ArrayList<String>(songHistories.size());
        for (SongHistory songHistory : songHistories) {
            //@formatter:off
            lines.add(
                    songHistory.getArtistName() + "\t" 
                    + songHistory.getAlbumName() + "\t" 
                    + songHistory.getSongName() + "\t"
                    + songHistory.getLatestPlayCount() + "\t"
                    + calculatePlayDifference(songHistory));
            //@formatter:on
        }

        return lines;
    }

    private int calculatePlayDifference(SongHistory songHistory) {
        if (songHistory.getEarliestPlayCount() != null
                && songHistory.getLatestPlayCount() != null) {
            return songHistory.getLatestPlayCount() - songHistory.getEarliestPlayCount();
        } else if (songHistory.getLatestPlayCount() != null) {
            return songHistory.getLatestPlayCount();
        } else {
            return 0 - songHistory.getEarliestPlayCount();
        }

    }
}
