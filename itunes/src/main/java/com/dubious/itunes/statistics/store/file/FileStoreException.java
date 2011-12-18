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

    public FileStoreException(String message) {
        super(message);
    }

    public FileStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
