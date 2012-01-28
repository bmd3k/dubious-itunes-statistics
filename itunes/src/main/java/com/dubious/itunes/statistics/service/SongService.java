package com.dubious.itunes.statistics.service;

import java.util.List;

import com.dubious.itunes.model.Album;
import com.dubious.itunes.model.Song;

/**
 * Service for retrieving information about artists, albums, and songs.
 */
public interface SongService {

    /**
     * Retrieve all albums in the system.
     * 
     * @return All albums.
     */
    List<Album> getAllAlbums();

    /**
     * Retrieve all songs for an album.
     * 
     * @param album The album.
     * @return The songs of the album.
     */
    List<Song> getSongsForAlbum(Album album);
}
