package com.dubious.itunes.statistics.service;

import static org.apache.commons.io.FileUtils.writeLines;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dubious.itunes.model.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;

public class AnalysisServiceImpl implements AnalysisService {

    @Override
    public void writeAnalysis(SnapshotsHistory history, String outputPath)
            throws StatisticsException {
        try {
            writeLines(new File(outputPath), "UTF-8", getDataToWrite(history), false);
        } catch (Throwable t) {
            throw new StatisticsException("Unexpected error: ", t);
        }
    }

    private List<String> getDataToWrite(SnapshotsHistory history) throws IOException {
        List<String> lines = new ArrayList<String>(history.getSongHistories().size());
        for (SongHistory songHistory : history.getSongHistories()) {
            Integer playDifference = songHistory.getLatestPlayCount();
            if (playDifference != null && songHistory.getEarliestPlayCount() != null) {
                playDifference -= songHistory.getEarliestPlayCount();
            }

            //@formatter:off
            lines.add(
                    songHistory.getArtistName() + "\t" 
                    + songHistory.getAlbumName() + "\t" 
                    + songHistory.getSongName() + "\t"
                    + songHistory.getLatestPlayCount() + "\t"
                    + playDifference);
            //@formatter:on
        }

        return lines;
    }
}
