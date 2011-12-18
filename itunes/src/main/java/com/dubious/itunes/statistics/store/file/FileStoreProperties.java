package com.dubious.itunes.statistics.store.file;

/**
 * Configuration properties for the file-based store.
 */
public class FileStoreProperties {

    private String fileStorePath;
    private String charset;

    public FileStoreProperties(String fileStorePath, String charset) {
        this.fileStorePath = fileStorePath;
        this.charset = charset;
    }

    public String getFileStorePath() {
        return fileStorePath;
    }

    public String getCharset() {
        return charset;
    }
}
