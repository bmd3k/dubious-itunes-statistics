package com.dubious.itunes.statistics.store.file;

import static org.apache.commons.io.FileUtils.lineIterator;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.io.LineIterator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.store.ReadOnlySnapshotStore;
import com.dubious.itunes.statistics.store.StoreException;

/**
 * File-based storage of snapshots.
 */
public class FileSnapshotStore implements ReadOnlySnapshotStore {

    private FileStoreProperties fileStoreProperties;

    private static final Integer COLUMN_INDEX_SONG_NAME = 0;
    private static final Integer COLUMN_INDEX_ARTIST_NAME = 1;
    private static final Integer COLUMN_INDEX_ALBUM_NAME = 3;
    private static final Integer COLUMN_INDEX_PLAY_COUNT = 21;

    private static final String COLUMN_SONG_NAME = "Name";
    private static final String COLUMN_ARTIST_NAME = "Artist";
    private static final String COLUMN_ALBUM_NAME = "Album";
    // Note that some versions of the file have "Plays" column while others have "Play Count" column
    private static final String COLUMN_PLAYS = "Plays";
    private static final String COLUMN_PLAY_COUNT = "Play Count";

    /**
     * Constructor.
     * 
     * @param fileStoreProperties {@link FileStoreProperties} to inject.
     */
    public FileSnapshotStore(FileStoreProperties fileStoreProperties) {
        this.fileStoreProperties = fileStoreProperties;
    }

    @Override
    public final Snapshot getSnapshot(String snapshotName) throws StoreException {
        LineIterator lines = null;
        try {
            // load file from the file store
            File snapshotFile =
                    new File(fileStoreProperties.getFileStorePath() + "/" + snapshotName);
            if (!snapshotFile.exists()) {
                return null;
            }

            try {
                lines = lineIterator(snapshotFile, fileStoreProperties.getCharset());
            } catch (FileNotFoundException e) {
                return null;
            }

            // assume that the snapshot date is written in the first 6 characters of the name
            DateTime snapshotDate =
                    DateTime.parse(snapshotName.substring(0, 6),
                            DateTimeFormat.forPattern("yyMMdd"));
            Snapshot snapshot = new Snapshot().withName(snapshotName).withDate(snapshotDate);

            // first line is the header, which we use as a bit of a sanity
            if (!lines.hasNext()) {
                throw new FileStoreException("File contains no information.");
            }
            checkHeader(lines.nextLine());
            // other lines represent unique songs. parse the lines and convert them into song
            // statistics.
            while (lines.hasNext()) {
                String line = lines.next();
                StatisticsFromLine statisticsFromLine = generateSongStatisticsFromLine(line);
                snapshot
                        .addStatistic(statisticsFromLine.song, statisticsFromLine.songStatistics);
            }

            return snapshot;
        } catch (FileStoreException e) {
            throw e;
        } catch (Throwable t) {
            throw new FileStoreException("Unexpected error in file store.", t);
        } finally {
            if (lines != null) {
                lines.close();
            }
        }
    }

    /**
     * Check the header line of the file to ensure columns are where they are expected to be.
     * 
     * @param headerLine The header from the file, to be checked.
     * @throws FileStoreException When the check fails.
     */
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
        if (!columns[COLUMN_INDEX_PLAY_COUNT].equals(COLUMN_PLAYS)
                && !columns[COLUMN_INDEX_PLAY_COUNT].equals(COLUMN_PLAY_COUNT)) {
            // older versions of the file use a different column name
            throw new FileStoreException("Column [Plays] does not exist where it should exist.");
        }
    }

    /**
     * Generate the statistics for a song from a line.
     * 
     * @param line The line.
     * @return The statistics extracted from the line.
     */
    private StatisticsFromLine generateSongStatisticsFromLine(String line) {
        String[] columns = line.split("\t", -1);
        return new StatisticsFromLine(new Song()
                .withArtistName(columns[COLUMN_INDEX_ARTIST_NAME])
                .withAlbumName(columns[COLUMN_INDEX_ALBUM_NAME])
                .withName(columns[COLUMN_INDEX_SONG_NAME]),
                new SongStatistics().withPlayCount(Integer
                        .parseInt(columns[COLUMN_INDEX_PLAY_COUNT])));
    }

    /**
     * Represents parts of song statistics.
     */
    private final class StatisticsFromLine {
        private Song song;
        private SongStatistics songStatistics;

        /**
         * Constructor.
         * 
         * @param song The song.
         * @param songStatistics The song statistics.
         */
        private StatisticsFromLine(Song song, SongStatistics songStatistics) {
            this.song = song;
            this.songStatistics = songStatistics;
        }
    }

}
