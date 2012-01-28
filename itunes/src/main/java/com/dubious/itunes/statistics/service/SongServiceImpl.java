package com.dubious.itunes.statistics.service;

import java.util.List;

import com.dubious.itunes.model.Album;
import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.AlbumDoesNotExistException;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.store.SongStore;

/**
 * Implementation of {@link SongService}.
 */
public class SongServiceImpl implements SongService {

    private SongStore songStore;

    /**
     * Constructor.
     * 
     * @param songStore {@link SongStore} to inject.
     */
    public SongServiceImpl(SongStore songStore) {
        this.songStore = songStore;
    }

    @Override
    public final List<Album> getAllAlbums() {
        return songStore.getAllAlbums();
    }

    @Override
    public final List<Song> getSongsForAlbum(String artistName, String albumName)
            throws StatisticsException {
        List<Song> songs = songStore.getSongsForAlbum(artistName, albumName);
        if (songs.size() == 0) {
            throw new AlbumDoesNotExistException(artistName, albumName);
        }
        return songs;
    }
}
