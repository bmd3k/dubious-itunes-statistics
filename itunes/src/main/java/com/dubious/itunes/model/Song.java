package com.dubious.itunes.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * A song.
 */
public class Song {

    // TODO: Things to consider:

    // TODO: things to consider when loading from file:
    // TODO: is album and track number available?
    // TODO: album available, track number not available
    // TODO: album not available, track number available
    // TODO: album not available, track number not available
    // TODO: only 1 to 3 songs in album (maybe indicates a problem in the data)

    // TODO: things to consider in save and load with mongo:
    // TODO: simple case: both album and track number available
    // TODO: album not available
    // TODO: album available but track not available
    // TODO: Never should have album not available but track available (check for this condition)
    // TODO: how do display album with 1 to 3 songs?

    private String artistName;
    private String albumName;
    private String name;
    private Integer trackNumber;

    /**
     * Name of the artist.
     * 
     * @param artistName Name of the artist.
     * @return This.
     */
    public final Song withArtistName(String artistName) {
        this.artistName = artistName;
        return this;
    }

    /**
     * Name of the album.
     * 
     * @param albumName Name of the album.
     * @return This.
     */
    public final Song withAlbumName(String albumName) {
        this.albumName = albumName;
        return this;
    }

    /**
     * Name of the song.
     * 
     * @param name Name of the song.
     * @return This.
     */
    public final Song withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Track number of the song within the album.
     * 
     * @param trackNumber Track number of the song within the album.
     * @return This
     */
    public final Song withTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
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
    public final String getAlbumName() {
        return albumName;
    }

    /**
     * Get the name of the song.
     * 
     * @return Name of the song.
     */
    public final String getName() {
        return name;
    }

    /**
     * Get the track number of the song within the album.
     * 
     * @return track number of the song within the album.
     */
    public final Integer getTrackNumber() {
        return trackNumber;
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
