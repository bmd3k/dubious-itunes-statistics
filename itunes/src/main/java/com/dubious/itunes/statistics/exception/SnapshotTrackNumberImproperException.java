package com.dubious.itunes.statistics.exception;

/**
 * Exception for case where snapshot does not specify album name but does specify a track number.
 */
public class SnapshotTrackNumberImproperException extends StatisticsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private String artistName;
    private String songName;

    /**
     * Constructor.
     * 
     * @param name Name of the snapshot.
     * @param artistName Artist name.
     * @param songName Song name.
     */
    public SnapshotTrackNumberImproperException(String name, String artistName, String songName) {
        super("Snapshot with name [" + name + "] contains song [" + artistName + "," + songName
                + "] that does not specify album name but specifies a track number.");
        this.name = name;
        this.artistName = artistName;
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
     * Song name.
     * 
     * @return Song name.
     */
    public final String getSongName() {
        return songName;
    }
}
