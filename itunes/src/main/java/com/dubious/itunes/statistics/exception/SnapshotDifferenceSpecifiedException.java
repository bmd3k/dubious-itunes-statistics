package com.dubious.itunes.statistics.exception;

/**
 * Exception for case where snapshot specifies a difference.
 */
public class SnapshotDifferenceSpecifiedException extends StatisticsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private String artistName;
    private String albumName;
    private String songName;

    /**
     * Constructor.
     * 
     * @param name Name of the snapshot.
     * @param artistName Artist name.
     * @param albumName Album name.
     * @param songName Song name.
     */
    public SnapshotDifferenceSpecifiedException(String name,
            String artistName,
            String albumName,
            String songName) {
        super("Snapshot with name [" + name + "] contains song [" + artistName + "," + albumName
                + "," + songName + "] that specifies a difference.  It should not.");
        this.name = name;
        this.artistName = artistName;
        this.albumName = albumName;
        this.songName = songName;
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

    /**
     * Song name.
     * 
     * @return Song name.
     */
    public final String getSongName() {
        return songName;
    }
}
