package com.dubious.itunes.model;

import org.joda.time.DateTime;

/**
 * A song.
 */
public class Song {

    private String artistName;
    private String albumName;
    private String name;
    private DateTime addDate;

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

    public Song withAddDate(DateTime addDate) {
        this.addDate = addDate;
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

    public DateTime getAddDate() {
        return addDate;
    }

}
