package com.dubious.itunes.model;

/**
 * An album.
 */
public class Album {
    private String name;

    /**
     * Set the name of the album.
     * 
     * @param name Name of the album.
     * @return This.
     */
    public final Album withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the name of the album.
     * 
     * @return The name of the album.
     */
    public final String getName() {
        return name;
    }
}
