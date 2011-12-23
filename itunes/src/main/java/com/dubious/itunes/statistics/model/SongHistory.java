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
    private Integer firstPlayCount;
    private Integer secondPlayCount;

    public SongHistory withArtistName(String artistName) {
        this.artistName = artistName;
        return this;
    }

    public SongHistory withAlbumName(String albumName) {
        this.albumName = albumName;
        return this;
    }

    public SongHistory withSongName(String songName) {
        this.songName = songName;
        return this;
    }

    public SongHistory withEarliestPlayCount(Integer firstPlayCount) {
        this.firstPlayCount = firstPlayCount;
        return this;
    }

    public SongHistory withLatestPlayCount(Integer secondPlayCount) {
        this.secondPlayCount = secondPlayCount;
        return this;
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

    public Integer getEarliestPlayCount() {
        return firstPlayCount;
    }

    public Integer getLatestPlayCount() {
        return secondPlayCount;
    }

    @Override
    public boolean equals(Object refactor) {
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
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
