package com.dubious.itunes.statistics;

/**
 * Base exception class for the iTunes statistics project.
 */
public class StatisticsException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param message Exception message.
     */
    public StatisticsException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * 
     * @param message Exception message.
     * @param cause Underlying cause of the exception.
     */
    public StatisticsException(String message, Throwable cause) {
        super(message, cause);
    }

}
