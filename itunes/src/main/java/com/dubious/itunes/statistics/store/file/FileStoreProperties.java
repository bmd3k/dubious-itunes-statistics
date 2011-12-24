package com.dubious.itunes.statistics.store.file;

/**
 * Configuration properties for the file-based store.
 */
public class FileStoreProperties {

    private String fileStorePath;
    private String charset;

    /**
     * Constructor.
     * 
     * @param fileStorePath Path on disk to the file storage.
     * @param charset The charset to use.
     */
    public FileStoreProperties(String fileStorePath, String charset) {
        this.fileStorePath = fileStorePath;
        this.charset = charset;
    }

    /**
     * Get the path on disk to the file storage.
     * 
     * @return Path on disk to the file storage.
     */
    public final String getFileStorePath() {
        return fileStorePath;
    }

    /**
     * Get the charset to use to write files.
     * 
     * @return Charset to use.
     */
    public final String getCharset() {
        return charset;
    }
}
