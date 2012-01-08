package com.dubious.itunes.statistics.service;

import static org.apache.commons.io.FileUtils.writeLines;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.dubious.itunes.statistics.exception.FileCouldNotBeWrittenException;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.model.SongStatistics;

/**
 * Implementation of snapshot-based statistical analysis.
 */
public class AnalysisServiceImpl implements AnalysisService {

    @Override
    public final void writeAnalysis(SnapshotsHistory history, String outputPath)
            throws StatisticsException {
        writeAnalysis(history.getSongHistories(), history.getSnapshots().get(0), history
                .getSnapshots()
                .get(history.getSnapshots().size() - 1), outputPath);
    }

    @Override
    public final void writeAnalysisOrderByDifference(
            final SnapshotsHistory history,
            String outputPath) throws StatisticsException {
        // need to make a copy of the list to be sorted as the one passed to us is unmodifiable
        List<SongHistory> sortedSongHistories =
                new ArrayList<SongHistory>(history.getSongHistories());
        Collections.sort(sortedSongHistories, new Comparator<SongHistory>() {

            @Override
            public int compare(SongHistory songHistory1, SongHistory songHistory2) {
                return calculatePlayDifference(songHistory2,
                        history.getSnapshots().get(0),
                        history.getSnapshots().get(history.getSnapshots().size() - 1))
                        - calculatePlayDifference(songHistory1,
                                history.getSnapshots().get(0),
                                history.getSnapshots().get(history.getSnapshots().size() - 1));
            }
        });
        writeAnalysis(sortedSongHistories, history.getSnapshots().get(0), history
                .getSnapshots()
                .get(history.getSnapshots().size() - 1), outputPath);
    }

    /**
     * Write analysis to file.
     * 
     * @param songHistories The histories to use in the analysis.
     * @param earliestSnapshot The earliest snapshot to consider.
     * @param latestSnapshot The latest snapshot to consider.
     * @param outputPath The file output path.
     * @throws StatisticsException On error.
     */
    private void writeAnalysis(
            List<SongHistory> songHistories,
            String earliestSnapshot,
            String latestSnapshot,
            String outputPath) throws StatisticsException {
        try {
            writeLines(new File(outputPath),
                    "UTF-8",
                    getDataToWrite(songHistories, earliestSnapshot, latestSnapshot),
                    false);
        } catch (IOException t) {
            throw new FileCouldNotBeWrittenException(outputPath, t);
        }
    }

    /**
     * Retrieve the analysis to write.
     * 
     * @param songHistories The histories to use in the analysis.
     * @param earliestSnapshot The earliest snapshot to consider.
     * @param latestSnapshot The latest snapshot to consider.
     * @return The lines to write.
     */
    private List<String> getDataToWrite(
            List<SongHistory> songHistories,
            String earliestSnapshot,
            String latestSnapshot) {
        List<String> lines = new ArrayList<String>(songHistories.size());
        for (SongHistory songHistory : songHistories) {
            //@formatter:off
            lines.add(
                    songHistory.getArtistName() + "\t" 
                    + songHistory.getAlbumName() + "\t" 
                    + songHistory.getSongName() + "\t"
                    + songHistory.getSongStatistics().get(latestSnapshot).getPlayCount() + "\t"
                    + calculatePlayDifference(songHistory, earliestSnapshot, latestSnapshot));
            //@formatter:on
        }

        return lines;
    }

    /**
     * Calculate the play difference for two snapshots in a song history.
     * 
     * @param songHistory The song history.
     * @param earliestSnapshot The earliest snapshot to consider.
     * @param latestSnapshot The latest snapshot to consider.
     * @return The difference in play counts between earliest and latest snapshots.
     */
    private int calculatePlayDifference(
            SongHistory songHistory,
            String earliestSnapshot,
            String latestSnapshot) {
        SongStatistics earliest = songHistory.getSongStatistics().get(earliestSnapshot);
        SongStatistics latest = songHistory.getSongStatistics().get(latestSnapshot);

        if (earliest != null && latest != null) {
            return latest.getPlayCount() - earliest.getPlayCount();
        } else if (latest != null) {
            return latest.getPlayCount();
        } else if (earliest != null) {
            return 0 - earliest.getPlayCount();
        } else {
            return 0;
        }
    }
}
