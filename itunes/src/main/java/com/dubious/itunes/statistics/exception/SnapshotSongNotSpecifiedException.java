package com.dubious.itunes.statistics.exception;

/**
 * Exception for case where snapshot contains a null song.
 */
public class SnapshotSongNotSpecifiedException extends StatisticsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;

    /**
     * Constructor.
     * 
     * @param name Snapshot name.
     */
    public SnapshotSongNotSpecifiedException(String name) {
        super("Snapshot with name [" + name + "] contains null song.");
        this.name = name;
    }

    /**
     * Snapshot name.
     * 
     * @return Snapshot name.
     */
    public final String getName() {
        return name;
    }
}
