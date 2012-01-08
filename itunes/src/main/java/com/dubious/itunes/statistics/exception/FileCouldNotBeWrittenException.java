package com.dubious.itunes.statistics.exception;

/**
 * Exception for when files cannot be written to disk.
 */
public class FileCouldNotBeWrittenException extends StatisticsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param outputPath The path to which the write failed.
     * @param cause The cause.
     */
    public FileCouldNotBeWrittenException(String outputPath, Throwable cause) {
        super("Could not write to path [" + outputPath + "]", cause);
    }
}
