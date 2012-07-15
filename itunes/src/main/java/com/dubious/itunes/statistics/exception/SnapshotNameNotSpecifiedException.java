package com.dubious.itunes.statistics.exception;

/**
 * Exception for case where snapshot does not specify a name.
 */
public class SnapshotNameNotSpecifiedException extends StatisticsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public SnapshotNameNotSpecifiedException() {
        super("Snapshot does not specify a name.");
    }
}
