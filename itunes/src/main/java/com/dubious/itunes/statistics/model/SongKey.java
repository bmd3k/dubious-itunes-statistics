package com.dubious.itunes.statistics.model;

/**
 * Describes the functional key of a Song when referred to by other objects.
 * 
 * @param <T>
 */
public abstract class SongKey<T extends SongKey<T>> {

    private String artistName;
    private String albumName;
    private String songName;

    public T withArtistName(String artistName) {
        this.artistName = artistName;
        return getThis();
    }

    public T withAlbumName(String albumName) {
        this.albumName = albumName;
        return getThis();
    }

    public T withSongName(String songName) {
        this.songName = songName;
        return getThis();
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getSongName() {
        return songName;
    }

    public abstract T getThis();
}
