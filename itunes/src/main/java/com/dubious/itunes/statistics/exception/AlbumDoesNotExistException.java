package com.dubious.itunes.statistics.exception;


/**
 * Exception for when an album does not exist.
 */
public class AlbumDoesNotExistException extends StatisticsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String artistName;
    private String albumName;

    /**
     * Constructor.
     * 
     * @param artistName Name of the artist.
     * @param albumName Name of the album.
     */
    public AlbumDoesNotExistException(String artistName, String albumName) {
        super("Album [" + artistName + "," + albumName + "] does not exist.");
        this.artistName = artistName;
        this.albumName = albumName;
    }

    /**
     * Retrieve the artist name.
     * 
     * @return The artist name.
     */
    public final String getArtistName() {
        return artistName;
    }

    /**
     * Retrieve the album name.
     * 
     * @return The album name.
     */
    public final String getAlbumName() {
        return albumName;
    }
}
