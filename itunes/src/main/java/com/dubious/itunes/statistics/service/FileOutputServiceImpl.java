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
import com.dubious.itunes.statistics.exception.UnexpectedStatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;

/**
 * Write snapshot history to file.
 */
public class FileOutputServiceImpl implements FileOutputService {

    private AnalysisService analysisService;

    /**
     * Constructor.
     * 
     * @param analysisService {@link AnalysisService} to inject.
     */
    public FileOutputServiceImpl(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @Override
    public final void writeSnapshotsHistory(
            SnapshotsHistory history,
            String outputPath,
            Order order) throws StatisticsException {
        Comparator<SongHistory> comparator = null;
        if (order == Order.PlayCount) {
            comparator =
                    new PlayCountComparator(history.getSnapshots().get(
                            history.getSnapshots().size() - 1));
        } else if (order == Order.Difference) {
            comparator =
                    new DifferenceComparator(history.getSnapshots().get(0), history
                            .getSnapshots()
                            .get(history.getSnapshots().size() - 1));
        } else {
            throw new UnexpectedStatisticsException("Unknown Order type specified");
        }

        writeSnapshotsHistory(history, outputPath, comparator);
    }

    /**
     * Write history to file.
     * 
     * @param history The history data for which to write the analysis.
     * @param outputPath The file output path.
     * @param comparator Describes how to sort the songs in the output of the analysis.
     * @throws StatisticsException On error.
     */
    public final void writeSnapshotsHistory(
            SnapshotsHistory history,
            String outputPath,
            Comparator<SongHistory> comparator) throws StatisticsException {
        SnapshotsHistory enrichedHistory = analysisService.enrichSnapshotsHistory(history);
        List<SongHistory> sortedSongHistories =
                new ArrayList<SongHistory>(enrichedHistory.getSongHistories());
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
            line.append("("
                    + songHistory.getSongStatistics().get(snapshots.get(0)).getDifference()
                    + ")\t");

            // write differences in play count between each snapshot
            line.append("(");
            int index = 0;
            int totalDifference = 0;
            for (index = 1; index < snapshots.size(); index++) {
                int difference =
                        songHistory
                                .getSongStatistics()
                                .get(snapshots.get(index))
                                .getDifference();
                totalDifference += difference;
                line.append(difference + ",");
            }
            line.deleteCharAt(line.length() - 1);
            // if have more than one difference then output a total of the differences.
            if (index > 2) {
                line.append("=" + totalDifference);
            }
            line.append(")\t");

            // write playcount from the latest snapshot ;
            line.append("("
                    + songHistory
                            .getSongStatistics()
                            .get(snapshots.get(snapshots.size() - 1))
                            .getPlayCount() + ")");

            lines.add(line.toString());
        }

        return lines;
    }
}
