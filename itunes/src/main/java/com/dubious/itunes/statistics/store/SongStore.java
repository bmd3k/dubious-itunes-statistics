package com.dubious.itunes.statistics.store;

import java.util.Collection;
import java.util.List;

import com.dubious.itunes.model.Album;
import com.dubious.itunes.model.Song;

/**
 * Read artists, albums, and songs from storage.
 */
public interface SongStore {

    /**
     * Write song data.
     * 
     * @param songs The song data to write.
     */
    void writeSongs(Collection<Song> songs);

    /**
     * Retrieve all albums in the system.
     * 
     * @return All albums in the system.
     */
    List<Album> getAllAlbums();

    /**
     * Retrieve all songs by artist and album.
     * 
     * @param artistName The artist name.
     * @param albumName The album name.
     * @return The songs for the (artist,album) combination.
     */
    List<Song> getSongsByArtistAndAlbum(String artistName, String albumName);

    /**
     * Retrieve songs by album.
     * 
     * @param albumName The album name.
     * @return The songs for the album.
     */
    List<Song> getSongsByAlbum(String albumName);
}
