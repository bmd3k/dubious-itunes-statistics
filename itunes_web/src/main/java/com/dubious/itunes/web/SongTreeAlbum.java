package com.dubious.itunes.web;

/**
 * Top-level item in a Song Tree.
 */
public class SongTreeAlbum {

    private String artistName;
    private String albumName;

    /**
     * Set the artist name.
     * 
     * @param artistName artist name.
     * @return this.
     */
    public final SongTreeAlbum withArtistName(String artistName) {
        this.artistName = artistName;
        return this;
    }

    /**
     * Set the album name.
     * 
     * @param albumName album name.
     * @return this.
     */
    public final SongTreeAlbum withAlbumName(String albumName) {
        this.albumName = albumName;
        return this;
    }

    /**
     * Retrieve the artist name.
     * 
     * @return artist name.
     */
    public final String getArtistName() {
        return artistName;
    }

    /**
     * Retrieve the album name.
     * 
     * @return album name.
     */
    public final String getAlbumName() {
        return albumName;
    }
}
