package com.dubious.itunes.statistics.service;

import com.dubious.itunes.statistics.model.SnapshotComparison;

/**
 * Describes services for statistical analysis.
 */
public interface AnalysisService {

    SnapshotComparison compareSnapshots(String snapshot1, String snapshot2);
}
