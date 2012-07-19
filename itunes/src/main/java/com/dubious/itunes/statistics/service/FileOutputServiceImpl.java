package com.dubious.itunes.statistics.service;

import static org.apache.commons.io.FileUtils.writeLines;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dubious.itunes.statistics.exception.FileCouldNotBeWrittenException;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.model.SongHistory;
import com.dubious.itunes.statistics.service.AnalysisService.Order;

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
            List<String> snapshots,
            String outputPath,
            Order order) throws StatisticsException {
        SnapshotsHistory enrichedHistory =
                analysisService.getEnrichedSnapshotsHistory(snapshots, order);

        try {
            writeLines(
                    new File(outputPath),
                    "UTF-8",
                    getDataToWrite(
                            enrichedHistory.getSnapshots(),
                            enrichedHistory.getSongHistories()),
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
