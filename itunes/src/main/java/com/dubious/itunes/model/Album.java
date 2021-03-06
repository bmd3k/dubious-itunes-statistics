package com.dubious.itunes.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * An album.
 */
public class Album {

    private String artistName;
    private String name;
    private Integer songCount;

    /**
     * Name of the artist.
     * 
     * @param artistName Name of the artist.
     * @return This.
     */
    public final Album withArtistName(String artistName) {
        this.artistName = artistName;
        return this;
    }

    /**
     * Name of the album.
     * 
     * @param name Name of the album.
     * @return This.
     */
    public final Album withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Number of songs in the album.
     * 
     * @param songCount Number of songs in the album.
     * @return This.
     */
    public final Album withSongCount(Integer songCount) {
        this.songCount = songCount;
        return this;
    }

    /**
     * Get the name of the artist.
     * 
     * @return Name of the artist.
     */
    public final String getArtistName() {
        return artistName;
    }

    /**
     * Get the name of the album.
     * 
     * @return Name of the album.
     */
    public final String getName() {
        return name;
    }

    /**
     * Get the number of songs in the album.
     * 
     * @return Number of songs in the album.
     */
    public final Integer getSongCount() {
        return songCount;
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
