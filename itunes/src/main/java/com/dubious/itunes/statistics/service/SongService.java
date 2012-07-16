package com.dubious.itunes.statistics.service;

import java.util.List;

import com.dubious.itunes.model.Album;
import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.StatisticsException;

/**
 * Service for retrieving information about artists, albums, and songs.
 */
public interface SongService {

    /**
     * Artist name of an album with more than one real artist.
     */
    String VARIOUS_ALBUM_ARTIST_NAME = "Various";

    /**
     * Retrieve all albums in the system.
     * 
     * @return All albums.
     */
    List<Album> getAllAlbums();

    /**
     * Retrieve all songs for an album.
     * 
     * @param artistName The name of the artist.
     * @param albumName The album name.
     * @return The songs of the album.
     * @throws StatisticsException On error.
     */
    List<Song> getSongsForAlbum(String artistName, String albumName) throws StatisticsException;
}
