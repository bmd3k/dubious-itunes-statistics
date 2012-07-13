package com.dubious.itunes.statistics.exception;

import com.dubious.itunes.statistics.service.YearAndQuarter;

/**
 * Exception thrown when attempting to filter snapshots by quarter but an expected quarter is not
 * represented.
 */
public class SnapshotForQuarterNotFoundException extends StatisticsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private YearAndQuarter quarter = null;

    /**
     * Constructor.
     * 
     * @param quarter The year and quarter that was not found.
     */
    public SnapshotForQuarterNotFoundException(YearAndQuarter quarter) {
        super("Could not find snapshot for quarter [" + quarter.getYear() + ", "
                + quarter.getQuarter() + "] while filtering.");
        this.quarter = quarter;
    }

    /**
     * Retrieve the year and quarter.
     * 
     * @return The year and quarter not found.
     */
    public final YearAndQuarter getQuarter() {
        return quarter;
    }
}
