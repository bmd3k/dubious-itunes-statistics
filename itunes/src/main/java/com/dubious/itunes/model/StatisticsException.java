package com.dubious.itunes.model;

public class StatisticsException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public StatisticsException(String message) {
        super(message);
    }

    public StatisticsException(String message, Throwable cause) {
        super(message, cause);
    }

}
