package com.dubious.itunes.statistics.store.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.StoreException;

/**
 * File-based storage of snapshots.
 */
public class SnapshotFileStore implements SnapshotStore {

    private FileStoreProperties fileStoreProperties;

    private static final Integer COLUMN_INDEX_SONG_NAME = 0;
    private static final Integer COLUMN_INDEX_ARTIST_NAME = 1;
    private static final Integer COLUMN_INDEX_ALBUM_NAME = 3;
    private static final Integer COLUMN_INDEX_PLAY_COUNT = 21;

    private static final String COLUMN_SONG_NAME = "Name";
    private static final String COLUMN_ARTIST_NAME = "Artist";
    private static final String COLUMN_ALBUM_NAME = "Album";
    private static final String COLUMN_PLAY_COUNT = "Plays";

    public SnapshotFileStore(FileStoreProperties fileStoreProperties) {
        this.fileStoreProperties = fileStoreProperties;
    }

    @Override
    public Snapshot getSnapshot(String snapshotName) throws StoreException {
        try {
            // load file from the file store
            File snapshotFile =
                    new File(fileStoreProperties.getFileStorePath() + "/" + snapshotName);
            if (!snapshotFile.exists()) {
                return null;
            }
            BufferedReader snapshotReader = null;
            try {
                snapshotReader =
                        new BufferedReader(
                                new InputStreamReader(new FileInputStream(snapshotFile),
                                        Charset.forName(fileStoreProperties.getCharset())));
            } catch (FileNotFoundException e) {
                return null;
            }

            // assume that the snapshot date is written in the first 6 characters of the name
            DateTime snapshotDate =
                    DateTime.parse(snapshotName.substring(0, 6),
                            DateTimeFormat.forPattern("yyMMdd"));
            Snapshot snapshot =
                    new Snapshot().withName(snapshotName).withSnapshotDate(snapshotDate);

            // first line is the header, which we use as a bit of a sanity
            String line = snapshotReader.readLine();
            if (line == null) {
                throw new FileStoreException("File contains no information.");
            }
            checkHeader(line);
            // other lines represent unique songs. parse the lines and convert them into song
            // statistics.
            while ((line = snapshotReader.readLine()) != null) {
                snapshot.addStatistic(generateSongStatisticsFromLine(line));
            }

            return snapshot;
        } catch (FileStoreException e) {
            throw e;
        } catch (Throwable t) {
            throw new FileStoreException("Unexpected error in file store.", t);
        }
    }

    private void checkHeader(String headerLine) throws FileStoreException {
        // check the fields of interest exist where we expect them to be
        String[] columns = headerLine.split("\t", -1);
        if (!columns[COLUMN_INDEX_SONG_NAME].equals(COLUMN_SONG_NAME)) {
            throw new FileStoreException("Column [Name] does not exist where it should exist.");
        }
        if (!columns[COLUMN_INDEX_ARTIST_NAME].equals(COLUMN_ARTIST_NAME)) {
            throw new FileStoreException("Column [Artist] does not exist where it should exist.");
        }
        if (!columns[COLUMN_INDEX_ALBUM_NAME].equals(COLUMN_ALBUM_NAME)) {
            throw new FileStoreException("Column [Album] does not exist where it should exist.");
        }
        if (!columns[COLUMN_INDEX_PLAY_COUNT].equals(COLUMN_PLAY_COUNT)) {
            throw new FileStoreException("Column [Plays] does not exist where it should exist.");
        }
    }

    private SongStatistics generateSongStatisticsFromLine(String line) {
        String[] columns = line.split("\t", -1);
        return new SongStatistics()
                .withArtistName(columns[COLUMN_INDEX_ARTIST_NAME])
                .withAlbumName(columns[COLUMN_INDEX_ALBUM_NAME])
                .withSongName(columns[COLUMN_INDEX_SONG_NAME])
                .withPlayCount(Integer.parseInt(columns[COLUMN_INDEX_PLAY_COUNT]));
    }

}
