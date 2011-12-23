package com.dubious.itunes.statistics.service;

import com.dubious.itunes.model.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;

public interface AnalysisService {

    void writeAnalysis(SnapshotsHistory history, String outputPath)
            throws StatisticsException;
}
