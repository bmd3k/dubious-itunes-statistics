package com.dubious.itunes.statistics.model;

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
    private Integer earliestPlayCount;
    private Integer latestPlayCount;

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
     * Set the playcount corresponding to the earliest snapshot.
     * 
     * @param earliestPlayCount Playcount corresponding to the earliest snapshot.
     * @return This.
     */
    public final SongHistory withEarliestPlayCount(Integer earliestPlayCount) {
        this.earliestPlayCount = earliestPlayCount;
        return this;
    }

    /**
     * Set the playcount corresponding to the latest snapshot.
     * 
     * @param latestPlayCount Playcount corresponding to the latest snapshot.
     * @return This.
     */
    public final SongHistory withLatestPlayCount(Integer latestPlayCount) {
        this.latestPlayCount = latestPlayCount;
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
     * Get the play count corresponding to the earliest snapshot.
     * 
     * @return Play count corresponding to the earliest snapshot.
     */
    public final Integer getEarliestPlayCount() {
        return earliestPlayCount;
    }

    /**
     * Get the play count corresponding to the latest snapshot.
     * 
     * @return Play count corresponding to the latest snapshot.
     */
    public final Integer getLatestPlayCount() {
        return latestPlayCount;
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
