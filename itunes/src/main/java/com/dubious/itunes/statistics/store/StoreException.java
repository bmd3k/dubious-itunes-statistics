package com.dubious.itunes.statistics.store;

/**
 * An exception in the storage layer. Specific implementations of the storage layer should extend
 * this class with their own implementation-specific exceptions.
 */
public abstract class StoreException extends Exception {

    private static final long serialVersionUID = 1L;

    public StoreException(String message) {
        super(message);
    }

    public StoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
