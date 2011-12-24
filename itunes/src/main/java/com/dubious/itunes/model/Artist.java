package com.dubious.itunes.model;

/**
 * An artist. The person or people who play the music.
 */
public class Artist {
    private String name;

    /**
     * Set the name of the artist.
     * 
     * @param name Name of the artist.
     * @return This.
     */
    public final Artist withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the name of the artist.
     * 
     * @return Name of the artist.
     */
    public final String getName() {
        return name;
    }
}
