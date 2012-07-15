package com.dubious.itunes.statistics.service;

import java.util.List;

import com.dubious.itunes.model.Album;

/**
 * Logic to regroup albums to handle the "Various" artist category of albums.
 */
public interface AlbumGrouping {

    /**
     * Artist name of an album with more than one real artist.
     */
    String GROUPED_ALBUM_ARTIST_NAME = "Various";

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
