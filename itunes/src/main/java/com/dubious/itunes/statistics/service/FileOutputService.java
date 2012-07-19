package com.dubious.itunes.statistics.service;

import java.util.List;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.service.AnalysisService.Order;

/**
 * Operations for writing analysis to file.
 */
public interface FileOutputService {

    /**
     * Write history to file. Elements are sorted based on latest play count, descending.
     * 
     * @param snapshots The snapshots to include in the history analysis.
     * @param outputPath The output path.
     * @param order The order.
     * @throws StatisticsException On error.
     */
    void writeSnapshotsHistory(List<String> snapshots, String outputPath, Order order)
            throws StatisticsException;

}
