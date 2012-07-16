package com.dubious.itunes.statistics.service;

import java.util.List;

import com.dubious.itunes.model.Song;

/**
 * Filters songs.
 */
public interface SongFilter {

    /**
     * Filter a songs using rules for "various" artist albums.
     * 
     * @param songs The songs to filter.
     * @return The filtered songs.
     */
    List<Song> filterForVariousAlbum(List<Song> songs);

}
