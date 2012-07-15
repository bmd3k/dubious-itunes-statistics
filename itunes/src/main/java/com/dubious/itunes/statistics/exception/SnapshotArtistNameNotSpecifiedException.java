package com.dubious.itunes.statistics.exception;

/**
 * Exception for case where snapshot contains song that does not specify an artist name.
 */
public class SnapshotArtistNameNotSpecifiedException extends StatisticsException {

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
    public SnapshotArtistNameNotSpecifiedException(String name) {
        super("Snapshot with name [" + name
                + "] contains song that does not specify an artist name.");
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
