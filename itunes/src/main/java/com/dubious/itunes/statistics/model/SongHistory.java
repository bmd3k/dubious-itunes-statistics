package com.dubious.itunes.statistics.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.dubious.itunes.model.Song;

/**
 * Statistical history of a song.
 */
public class SongHistory {

    private String artistName;
    private String albumName;
    private String songName;
    private Map<String, SongStatistics> songStatistics;

    /**
     * Constructor.
     */
    public SongHistory() {
        this.songStatistics = new LinkedHashMap<String, SongStatistics>();
    }

    /**
     * Set the name of the artist.
     * 
     * @param artistName Name of the artist.
     * @return This.
     */
    public final SongHistory withArtistName(String artistName) {
        this.artistName = artistName;
        return this;
    }

    /**
     * Set the name of the album.
     * 
     * @param albumName Name of the album.
     * @return This.
     */
    public final SongHistory withAlbumName(String albumName) {
        this.albumName = albumName;
        return this;
    }

    /**
     * Set the name of the song.
     * 
     * @param songName The name of the song.
     * @return This.
     */
    public final SongHistory withSongName(String songName) {
        this.songName = songName;
        return this;
    }

    /**
     * Populate song-specific information using a given song. This is a shortcut method.
     * 
     * @param song The song with the information.
     * @return This.
     */
    public final SongHistory withSong(Song song) {
        return withArtistName(song.getArtistName())
                .withAlbumName(song.getAlbumName())
                .withSongName(song.getName());
    }

    /**
     * Add statistics for the song from a snapshot.
     * 
     * @param snapshotName The name of the snapshot.
     * @param songStatistics The statistics of the song from the snapshot.
     * @return This.
     */
    public final SongHistory addSongStatistics(String snapshotName, SongStatistics songStatistics) {
        this.songStatistics.put(snapshotName, songStatistics);
        return this;
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

    /**
     * Get the map of snapshot to statistics.
     * 
     * @return The map of snapshot to statistics.
     */
    public final Map<String, SongStatistics> getSongStatistics() {
        return Collections.unmodifiableMap(songStatistics);
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
