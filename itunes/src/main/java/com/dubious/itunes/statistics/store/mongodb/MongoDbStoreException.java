package com.dubious.itunes.statistics.store.mongodb;

import com.dubious.itunes.statistics.store.StoreException;

/**
 * MongoDb-based storage exception.
 */
public class MongoDbStoreException extends StoreException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param message Exception message.
     */
    public MongoDbStoreException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * 
     * @param message Exception message.
     * @param cause Underlying cause of the exception.
     */
    public MongoDbStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
