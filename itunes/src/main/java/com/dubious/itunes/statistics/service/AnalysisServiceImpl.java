package com.dubious.itunes.statistics.service;

import static org.apache.commons.io.FileUtils.writeLines;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
    public final void writeAnalysis(final SnapshotsHistory history, String outputPath)
            throws StatisticsException {
        writeAnalysis(history, outputPath, new Comparator<SongHistory>() {
            @Override
            public int compare(SongHistory songHistory1, SongHistory songHistory2) {
                String latestSnapshot =
                        history.getSnapshots().get(history.getSnapshots().size() - 1);

                return calculatePlayDifference(
                        songHistory1.getSongStatistics().get(latestSnapshot),
                        songHistory2.getSongStatistics().get(latestSnapshot));
            }
        });
    }

    @Override
    public final void writeAnalysisOrderByDifference(
            final SnapshotsHistory history,
            String outputPath) throws StatisticsException {
        writeAnalysis(history, outputPath, new Comparator<SongHistory>() {
            @Override
            public int compare(SongHistory songHistory1, SongHistory songHistory2) {
                return calculatePlayDifference(
                        songHistory2,
                        history.getSnapshots().get(0),
                        history.getSnapshots().get(history.getSnapshots().size() - 1))
                        - calculatePlayDifference(
                                songHistory1,
                                history.getSnapshots().get(0),
                                history.getSnapshots().get(history.getSnapshots().size() - 1));
            }
        });
    }

    /**
     * Write analysis to file.
     * 
     * @param history The history data for which to write the analysis.
     * @param outputPath The file output path.
     * @param comparator Describes how to sort the songs in the output of the analysis.
     * @throws StatisticsException On error.
     */
    private void writeAnalysis(
            SnapshotsHistory history,
            String outputPath,
            Comparator<SongHistory> comparator) throws StatisticsException {
        List<SongHistory> sortedSongHistories =
                new ArrayList<SongHistory>(history.getSongHistories());
        Collections.sort(sortedSongHistories, comparator);

        try {
            writeLines(
                    new File(outputPath),
                    "UTF-8",
                    getDataToWrite(history.getSnapshots(), sortedSongHistories),
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
                        calculatePlayDifference(
                                songHistory,
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
        return calculatePlayDifference(
                songHistory.getSongStatistics().get(earliestSnapshot),
                songHistory.getSongStatistics().get(latestSnapshot));
    }

    /**
     * Calculate the play difference between two song statistics.
     * 
     * @param earliest The earliest statistics to consider.
     * @param latest The latest statistics to consider.
     * @return The difference in play counts between the earliest and latest statistics.
     */
    private int calculatePlayDifference(SongStatistics earliest, SongStatistics latest) {
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

    @Override
    public final SongHistory enrichSongHistory(SongHistory songHistory) {

        SongHistory newSongHistory =
                new SongHistory()
                        .withArtistName(songHistory.getArtistName())
                        .withAlbumName(songHistory.getAlbumName())
                        .withSongName(songHistory.getSongName());

        // note that we are relying on the fact that the underlying Map of the SongHistory object is
        // a LinkedHashMap and has well-defined order.
        Integer previousPlayCount = null;
        for (Map.Entry<String, SongStatistics> entry : songHistory
                .getSongStatistics()
                .entrySet()) {
            Integer playCount = 0;
            if (entry.getValue() != null && entry.getValue().getPlayCount() != null) {
                playCount = entry.getValue().getPlayCount();
            }
            // if the first element in the map then the difference is the play count, otherwise
            // it is the actual difference between the previous and current play count.
            Integer difference = playCount;
            if (previousPlayCount != null) {
                difference = playCount - previousPlayCount;
            }

            newSongHistory.addSongStatistics(
                    entry.getKey(),
                    new SongStatistics().withPlayCount(playCount).withDifference(difference));
            previousPlayCount = playCount;
        }

        return newSongHistory;
    }
}
