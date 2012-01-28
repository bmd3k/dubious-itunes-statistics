package com.dubious.itunes.web;

/**
 * Leaf item in the song tree.
 */
public class SongTreeSong {

    private String artistName;
    private String albumName;
    private String songName;

    /**
     * Set the artist name.
     * 
     * @param artistName the artist name.
     * @return this.
     */
    public final SongTreeSong withArtistName(String artistName) {
        this.artistName = artistName;
        return this;
    }

    /**
     * Set the album name.
     * 
     * @param albumName the album name.
     * @return this.
     */
    public final SongTreeSong withAlbumName(String albumName) {
        this.albumName = albumName;
        return this;
    }

    /**
     * Set the song name.
     * 
     * @param songName the song name.
     * @return this.
     */
    public final SongTreeSong withSongName(String songName) {
        this.songName = songName;
        return this;
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

    /**
     * Retrieve the song name.
     * 
     * @return The song name.
     */
    public final String getSongName() {
        return songName;
    }
}
