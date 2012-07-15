package com.dubious.itunes.statistics.validate;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;

/**
 * Describes validation of a snapshot.
 */
public interface SnapshotValidation {

    /**
     * Validate on write.
     * 
     * @param snapshot The snapshot to write.
     * @throws StatisticsException If any of the checks fail.
     */
    void validateOnWrite(Snapshot snapshot) throws StatisticsException;
}
