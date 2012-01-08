package com.dubious.itunes.statistics.exception;

import java.util.List;

/**
 * Exception for when an insufficient number of snapshots are specified to create a History.
 */
public class InsufficientSnapshotsSpecifiedException extends StatisticsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer minSnapshots;
    private List<String> snapshots;

    /**
     * Constructor.
     * 
     * @param minSnapshots The required minimium number of snapshots.
     * @param snapshots The specified snapshots.
     */
    public InsufficientSnapshotsSpecifiedException(Integer minSnapshots, List<String> snapshots) {
        super("An insufficient number of snapshots [" + snapshots.size()
                + "] were specified.  The minimum is [" + minSnapshots + "].");

        this.minSnapshots = minSnapshots;
        this.snapshots = snapshots;
    }

    /**
     * Retrieve the minimum number of snapshots.
     * 
     * @return The minimum number of snapshots.
     */
    public final Integer getMinSnapshots() {
        return minSnapshots;
    }

    /**
     * Retrieve the specified snapshots.
     * 
     * @return The specified snapshots.
     */
    public final List<String> getSnapshots() {
        return snapshots;
    }
}
