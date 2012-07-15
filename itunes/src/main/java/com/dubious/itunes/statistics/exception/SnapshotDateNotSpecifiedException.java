package com.dubious.itunes.statistics.exception;

/**
 * Exception for case where snapshot does not specify a date.
 */
public class SnapshotDateNotSpecifiedException extends StatisticsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;

    /**
     * Constructor.
     * 
     * @param name Name of the snapshot.
     */
    public SnapshotDateNotSpecifiedException(String name) {
        super("Snapshot with name [" + name + "] does not specify a date.");
        this.name = name;
    }

    /**
     * Get the name of the snapshot.
     * 
     * @return The name of the snapshot.
     */
    public final String getName() {
        return name;
    }
}
