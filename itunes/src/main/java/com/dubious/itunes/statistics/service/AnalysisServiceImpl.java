package com.dubious.itunes.statistics.service;

import static org.apache.commons.io.FileUtils.writeLines;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.dubious.itunes.statistics.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;

/**
 * Implementation of snapshot-based statistical analysis.
 */
public class AnalysisServiceImpl implements AnalysisService {

    @Override
    public final void writeAnalysis(SnapshotsHistory history, String outputPath)
            throws StatisticsException {
        writeAnalysis(history.getSongHistories(), outputPath);
    }

    @Override
    public final void
            writeAnalysisOrderByDifference(SnapshotsHistory history, String outputPath)
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

    /**
     * Write analysis to file.
     * 
     * @param songHistories The histories to use in the analysis.
     * @param outputPath The file output path.
     * @throws StatisticsException On error.
     */
    private void writeAnalysis(List<SongHistory> songHistories, String outputPath)
            throws StatisticsException {
        try {
            writeLines(new File(outputPath), "UTF-8", getDataToWrite(songHistories), false);
        } catch (Throwable t) {
            throw new StatisticsException("Unexpected error: ", t);
        }
    }

    /**
     * Retrieve the analysis to write.
     * 
     * @param songHistories The histories to use in the analysis.
     * @return The lines to write.
     */
    private List<String> getDataToWrite(List<SongHistory> songHistories) {
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

    /**
     * Calculate the play difference for a song history.
     * 
     * @param songHistory The song history.
     * @return The difference in play counts between earliest and latest snapshots.
     */
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
