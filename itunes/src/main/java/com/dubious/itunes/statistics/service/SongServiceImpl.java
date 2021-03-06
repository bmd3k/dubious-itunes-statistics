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
    private AlbumGrouping albumGrouping;
    private SongFilter songFilter;

    /**
     * Constructor.
     * 
     * @param albumGrouping {@link AlbumGrouping} to inject.
     * @param songFilter {@link SongFilter} to inject.
     * @param songStore {@link SongStore} to inject.
     */
    public SongServiceImpl(AlbumGrouping albumGrouping,
            SongFilter songFilter,
            SongStore songStore) {
        this.albumGrouping = albumGrouping;
        this.songFilter = songFilter;
        this.songStore = songStore;
    }

    @Override
    public final List<Album> getAllAlbums() {
        return albumGrouping.groupAlbums(songStore.getAllAlbums());
    }

    @Override
    public final List<Song> getSongsForAlbum(String artistName, String albumName)
            throws StatisticsException {
        List<Song> songs = null;
        if (artistName.equals(VARIOUS_ALBUM_ARTIST_NAME)) {
            songs = songFilter.filterForVariousAlbum(songStore.getSongsByAlbum(albumName));
        } else {
            songs = songStore.getSongsByArtistAndAlbum(artistName, albumName);
        }

        if (songs.size() == 0) {
            throw new AlbumDoesNotExistException(artistName, albumName);
        }
        return songs;
    }
}
