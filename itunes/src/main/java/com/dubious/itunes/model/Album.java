package com.dubious.itunes.model;

/**
 * An album.
 */
public class Album {
    private String name;

    public Album withName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }
}
