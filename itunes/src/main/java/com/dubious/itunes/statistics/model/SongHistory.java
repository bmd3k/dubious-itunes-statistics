package com.dubious.itunes.statistics.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Statistical history of a song.
 */
public class SongHistory {

    private String artistName;
    private String albumName;
    private String songName;
    private List<String> snapshotNames;
    private Map<String, SongStatistics> songStatistics;

    public SongHistory() {
        this.snapshotNames = new ArrayList<String>();
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

    public final SongHistory addSongStatistics(String snapshotName, SongStatistics songStatistics) {
        snapshotNames.add(snapshotName);
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

    public final List<String> getSnapshots() {
        return Collections.unmodifiableList(snapshotNames);
    }

    public final Map<String, SongStatistics> getSongStatistics() {
        return Collections.unmodifiableMap(songStatistics);
    }

    @Override
    public final boolean equals(Object refactor) {
        if (refactor == null) {
            return false;
        }
        if (refactor == this) {
            return true;
        }
        if (refactor.getClass() != getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, refactor);
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
