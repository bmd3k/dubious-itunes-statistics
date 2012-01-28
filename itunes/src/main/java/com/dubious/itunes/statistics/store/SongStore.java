package com.dubious.itunes.statistics.store;

import java.util.List;

import com.dubious.itunes.model.Album;
import com.dubious.itunes.model.Song;

/**
 * Read artists, albums, and songs from storage.
 */
public interface SongStore {

    /**
     * Retrieve all albums in the system.
     * 
     * @return All albums in the system.
     */
    List<Album> getAllAlbums();

    /**
     * Retrieve all songs for an album.
     * 
     * @param artistName The artist name.
     * @param albumName The album name.
     * @return The songs for the album.
     */
    List<Song> getSongsForAlbum(String artistName, String albumName);
}
