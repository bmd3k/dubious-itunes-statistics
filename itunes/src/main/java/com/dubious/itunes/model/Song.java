package com.dubious.itunes.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * A song.
 */
public class Song {

    private String artistName;
    private String albumName;
    private String name;

    public Song withArtistName(String artistName) {
        this.artistName = artistName;
        return this;
    }

    public Song withAlbumName(String albumName) {
        this.albumName = albumName;
        return this;
    }

    public Song withName(String name) {
        this.name = name;
        return this;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getName() {
        return name;
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
