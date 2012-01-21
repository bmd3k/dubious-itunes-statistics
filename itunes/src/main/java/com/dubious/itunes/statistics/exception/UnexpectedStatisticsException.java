package com.dubious.itunes.statistics.exception;

/**
 * Expresses unexpected internal errors.
 */
public class UnexpectedStatisticsException extends StatisticsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param message Message.
     */
    public UnexpectedStatisticsException(String message) {
        super(message);
    }
}
