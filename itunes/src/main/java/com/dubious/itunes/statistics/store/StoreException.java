package com.dubious.itunes.statistics.store;

import com.dubious.itunes.statistics.exception.StatisticsException;

/**
 * An exception in the storage layer.
 */
public abstract class StoreException extends StatisticsException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param message Exception message.
     */
    public StoreException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * 
     * @param message Exception message.
     * @param cause Underlying cause of the exception.
     */
    public StoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
