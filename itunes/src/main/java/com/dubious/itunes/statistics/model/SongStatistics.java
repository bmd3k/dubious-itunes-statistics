package com.dubious.itunes.statistics.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Statistics for a song for a specific snapshot.
 */
public class SongStatistics {
    private Integer playCount;

    /**
     * Set the play count for the song.
     * 
     * @param playCount Play count for the song.
     * @return This.
     */
    public final SongStatistics withPlayCount(Integer playCount) {
        this.playCount = playCount;
        return this;
    }

    /**
     * Get the play count for the song.
     * 
     * @return Play count for the song.
     */
    public final Integer getPlayCount() {
        return playCount;
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
