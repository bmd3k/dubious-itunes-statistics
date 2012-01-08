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
        writeAnalysis(history.getSnapshots(), history.getSongHistories(), outputPath);
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
        writeAnalysis(history.getSnapshots(), sortedSongHistories, outputPath);
    }

    /**
     * Write analysis to file.
     * 
     * @param snapshots The snapshots to consider in the analysis.
     * @param songHistories The histories to use in the analysis.
     * @param outputPath The file output path.
     * @throws StatisticsException On error.
     */
    private void writeAnalysis(
            List<String> snapshots,
            List<SongHistory> songHistories,
            String outputPath) throws StatisticsException {
        try {
            writeLines(new File(outputPath),
                    "UTF-8",
                    getDataToWrite(snapshots, songHistories),
                    false);
        } catch (IOException t) {
            throw new FileCouldNotBeWrittenException(outputPath, t);
        }
    }

    /**
     * Retrieve the analysis to write.
     * 
     * @param snapshots The snapshots to consider in the analysis.
     * @param songHistories The histories to use in the analysis.
     * @return The lines to write.
     */
    private List<String> getDataToWrite(List<String> snapshots, List<SongHistory> songHistories) {
        List<String> lines = new ArrayList<String>(songHistories.size());
        for (SongHistory songHistory : songHistories) {
            //@formatter:off
            StringBuilder line = new StringBuilder(
                    songHistory.getArtistName() + "\t" 
                    + songHistory.getAlbumName() + "\t" 
                    + songHistory.getSongName() + "\t");
            //@formatter:on

            // write play count from earliest snapshot
            SongStatistics earliestStatistics =
                    songHistory.getSongStatistics().get(snapshots.get(0));
            line.append(earliestStatistics == null ? "(0)" : "("
                    + earliestStatistics.getPlayCount() + ")");
            line.append("\t");

            // write differences in play count between each snapshot
            line.append("(");
            int index = 0;
            int totalDifference = 0;
            for (index = 1; index < snapshots.size(); index++) {
                int difference =
                        calculatePlayDifference(songHistory,
                                snapshots.get(index - 1),
                                snapshots.get(index));
                totalDifference += difference;
                line.append(difference + ",");
            }
            line.deleteCharAt(line.length() - 1);
            // if have more than one difference then output a total of the differences.
            if (index > 2) {
                line.append("=" + totalDifference);
            }
            line.append(")\t");

            // write playcount from the latest snapshot
            SongStatistics latestStatistics =
                    songHistory.getSongStatistics().get(snapshots.get(snapshots.size() - 1));
            line.append(latestStatistics == null ? "(0)" : "(" + latestStatistics.getPlayCount()
                    + ")");

            lines.add(line.toString());
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
