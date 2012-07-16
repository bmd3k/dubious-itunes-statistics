package com.dubious.itunes.statistics.service;

import java.util.List;

import com.dubious.itunes.model.Album;

/**
 * Logic to regroup albums to handle the "Various" artist category of albums.
 */
public interface AlbumGrouping {

    /**
     * The threshold that determines whether an (Artist,Album) combination should be included in a
     * Various album or not.
     */
    int GROUP_THRESHOLD = 4;

    /**
     * Regroup albums beyond the (Artist,Album) combination stored in the db. (Artist,Album)
     * combinations that only contain 3 or fewer items could be part of a larger album with various
     * artists.
     * 
     * @param albums The albums to regroup.
     * @return The list of regrouped albums.
     */
    List<Album> groupAlbums(List<Album> albums);
}
