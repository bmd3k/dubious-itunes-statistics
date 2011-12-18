package com.dubious.itunes.model;

/**
 * An artist. The person or people who play the music.
 */
public class Artist {
    private String name;

    public Artist withName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }
}
