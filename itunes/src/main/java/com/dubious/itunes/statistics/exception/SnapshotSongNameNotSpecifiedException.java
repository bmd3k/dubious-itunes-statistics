package com.dubious.itunes.statistics.exception;

/**
 * Exception for case where snapshot does not specify a song name.
 */
public class SnapshotSongNameNotSpecifiedException extends StatisticsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private String artistName;
    private String albumName;

    /**
     * Constructor.
     * 
     * @param name Name of the snapshot.
     * @param artistName Artist name.
     * @param albumName Album name.
     */
    public SnapshotSongNameNotSpecifiedException(String name, String artistName, String albumName) {
        super("Snapshot with name [" + name + "] contains song [" + artistName + "," + albumName
                + "] that does not specify song name.");
        this.name = name;
        this.artistName = artistName;
        this.albumName = albumName;
    }

    /**
     * Snapshot name.
     * 
     * @return Snapshot name.
     */
    public final String getName() {
        return name;
    }

    /**
     * Artist name.
     * 
     * @return Artist name.
     */
    public final String getArtistName() {
        return artistName;
    }

    /**
     * Album name.
     * 
     * @return Album name.
     */
    public final String getAlbumName() {
        return albumName;
    }
}
