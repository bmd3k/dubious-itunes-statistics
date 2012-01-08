package com.dubious.itunes.statistics.exception;

/**
 * Exception for when a snapshot does not exist.
 */
public class SnapshotDoesNotExistException extends StatisticsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String snapshot;

    /**
     * Constructor.
     * 
     * @param snapshot The snapshot that could not be found.
     */
    public SnapshotDoesNotExistException(String snapshot) {
        super("Snapshot [" + snapshot + "] does not exist");
        this.snapshot = snapshot;
    }

    /**
     * Retrieve the name of the snapshot that could not be found.
     * 
     * @return The name of the snapshot that could not be found.
     */
    public final String getSnapshot() {
        return snapshot;
    }
}
