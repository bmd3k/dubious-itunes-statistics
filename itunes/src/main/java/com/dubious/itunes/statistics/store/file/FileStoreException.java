package com.dubious.itunes.statistics.store.file;

import com.dubious.itunes.statistics.store.StoreException;

/**
 * File-based storage exception.
 */
public class FileStoreException extends StoreException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param message Exception message.
     */
    public FileStoreException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * 
     * @param message Exception message.
     * @param cause Underlying cause of the exception.
     */
    public FileStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
