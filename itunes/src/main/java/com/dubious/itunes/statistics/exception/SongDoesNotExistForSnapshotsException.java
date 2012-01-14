package com.dubious.itunes.statistics.exception;

import java.util.List;

/**
 * Exception for when a song does not exist.
 */
public class SongDoesNotExistForSnapshotsException extends StatisticsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String artistName;
    private String albumName;
    private String songName;
    private List<String> snapshotNames;

    /**
     * Constructor.
     * 
     * @param artistName Name of the artist.
     * @param albumName Name of the album.
     * @param songName Name of the song.
     * @param snapshotNames Names of the snapshots.
     */
    public SongDoesNotExistForSnapshotsException(String artistName,
            String albumName,
            String songName,
            List<String> snapshotNames) {
        super("Song [" + artistName + "," + albumName + "," + songName
                + "] does not exist for snapshots " + snapshotNames);
        this.artistName = artistName;
        this.albumName = albumName;
        this.songName = songName;
    }

    /**
     * Retrieve the artist name.
     * 
     * @return The artist name.
     */
    public final String getArtistName() {
        return artistName;
    }

    /**
     * Retrieve the album name.
     * 
     * @return The album name.
     */
    public final String getAlbumName() {
        return albumName;
    }

    /**
     * Retrieve the song name.
     * 
     * @return The song name.
     */
    public final String getSongName() {
        return songName;
    }

    /**
     * Retrieve the list of snapshot names.
     * 
     * @return The list of snapshot names.
     */
    public final List<String> getSnapshotNames() {
        return snapshotNames;
    }
}
