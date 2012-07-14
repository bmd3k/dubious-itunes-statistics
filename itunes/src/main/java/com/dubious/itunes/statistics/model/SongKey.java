package com.dubious.itunes.statistics.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.dubious.itunes.model.Song;

/**
 * Abstract class representing the functional key of a Song.
 * 
 * @param <T>
 */
public abstract class SongKey<T extends SongKey<T>> {

    private String artistName;
    private String albumName;
    private String songName;

    /**
     * Return this instance.
     * 
     * @return This.
     */
    public abstract T getThis();

    /**
     * Set the name of the artist.
     * 
     * @param artistName Name of the artist.
     * @return This.
     */
    public final T withArtistName(String artistName) {
        this.artistName = artistName;
        return getThis();
    }

    /**
     * Set the name of the album.
     * 
     * @param albumName Name of the album.
     * @return This.
     */
    public final T withAlbumName(String albumName) {
        this.albumName = albumName;
        return getThis();
    }

    /**
     * Set the name of the song.
     * 
     * @param songName The name of the song.
     * @return This.
     */
    public final T withSongName(String songName) {
        this.songName = songName;
        return getThis();
    }

    /**
     * Populate song-specific information using a given song. This is a shortcut method.
     * 
     * @param song The song with the information.
     * @return This.
     */
    public final T withSong(Song song) {
        withArtistName(song.getArtistName()).withAlbumName(song.getAlbumName()).withSongName(
                song.getName());
        return getThis();
    }

    /**
     * Get the artist name.
     * 
     * @return Artist name.
     */
    public final String getArtistName() {
        return artistName;
    }

    /**
     * Get the album name.
     * 
     * @return Album name.
     */
    public final String getAlbumName() {
        return albumName;
    }

    /**
     * Get the song name.
     * 
     * @return Song name.
     */
    public final String getSongName() {
        return songName;
    }

    @Override
    public final boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
