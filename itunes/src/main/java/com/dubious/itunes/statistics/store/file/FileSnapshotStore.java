package com.dubious.itunes.statistics.store.file;

import static org.apache.commons.io.FileUtils.lineIterator;
import static org.apache.commons.io.FileUtils.listFiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.LineIterator;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.StoreException;

/**
 * File-based storage of snapshots.
 */
public class FileSnapshotStore implements SnapshotStore {

    private FileStoreProperties fileStoreProperties;

    private static final Integer COLUMN_INDEX_SONG_NAME = 0;
    private static final Integer COLUMN_INDEX_ARTIST_NAME = 1;
    private static final Integer COLUMN_INDEX_ALBUM_NAME = 3;
    private static final Integer COLUMN_INDEX_PLAY_COUNT = 21;
    private static final Integer COLUMN_INDEX_TRACK_NUMBER = 10;

    private static final String COLUMN_SONG_NAME = "Name";
    private static final String COLUMN_ARTIST_NAME = "Artist";
    private static final String COLUMN_ALBUM_NAME = "Album";
    // Note that some versions of the file have "Plays" column while others have "Play Count" column
    private static final String COLUMN_PLAYS = "Plays";
    private static final String COLUMN_PLAY_COUNT = "Play Count";
    private static final String COLUMN_TRACK_NUMBER = "Track Number";

    private static final String MATCHING_FILE_PATTERN = "\\d\\d\\d\\d\\d\\d - Music.txt";

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

            Snapshot snapshot = getSnapshotBase(snapshotFile.getName());

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

    @Override
    public final List<Snapshot> getSnapshots() throws StoreException {
        try {
            List<Snapshot> snapshots = new ArrayList<Snapshot>();
            for (File snapshotFile : listSnapshotFiles()) {
                snapshots.add(getSnapshot(snapshotFile.getName()));
            }

            sortByDate(snapshots);
            return snapshots;
        } catch (FileStoreException e) {
            throw e;
        } catch (Throwable t) {
            throw new FileStoreException("Unexpected error in file store.", t);
        }
    }

    @Override
    public final List<Snapshot> getSnapshotsWithoutStatistics() throws StoreException {
        try {
            // find the files in the file store directory that are snapshots
            List<Snapshot> snapshots = new ArrayList<Snapshot>();
            for (File snapshotFile : listSnapshotFiles()) {
                snapshots.add(getSnapshotBase(snapshotFile.getName()));
            }

            sortByDate(snapshots);
            return snapshots;
        } catch (Throwable t) {
            throw new FileStoreException("Unexpected error in file store.", t);
        }
    }

    /**
     * List all snapshot files in the file store.
     * 
     * @return All snapshot files in the file store.
     */
    private Collection<File> listSnapshotFiles() {
        return listFiles(new File(fileStoreProperties.getFileStorePath()), new RegexFileFilter(
                MATCHING_FILE_PATTERN), null);
    }

    /**
     * Get the base snapshot information from a snapshot file.
     * 
     * @param fileName The name of the snapshot file.
     * @return Base snapshot information.
     */
    private Snapshot getSnapshotBase(String fileName) {
        // assume that the snapshot date is written in the first 6 characters of the name
        return new Snapshot().withName(fileName).withDate(
                DateTime.parse(fileName.substring(0, 6), DateTimeFormat.forPattern("yyMMdd")));
    }

    /**
     * Sort snapshots by their dates.
     * 
     * @param snapshots The snapshots to sort.
     */
    private void sortByDate(List<Snapshot> snapshots) {
        Collections.sort(snapshots, new Comparator<Snapshot>() {
            @Override
            public int compare(Snapshot snapshot1, Snapshot snapshot2) {
                return snapshot1.getDate().compareTo(snapshot2.getDate());
            }
        });
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
            throw new FileStoreException("Column [" + COLUMN_SONG_NAME
                    + "] does not exist where it should exist.");
        }
        if (!columns[COLUMN_INDEX_ARTIST_NAME].equals(COLUMN_ARTIST_NAME)) {
            throw new FileStoreException("Column [" + COLUMN_ARTIST_NAME
                    + "] does not exist where it should exist.");
        }
        if (!columns[COLUMN_INDEX_ALBUM_NAME].equals(COLUMN_ALBUM_NAME)) {
            throw new FileStoreException("Column [" + COLUMN_ALBUM_NAME
                    + "] does not exist where it should exist.");
        }
        if (!columns[COLUMN_INDEX_PLAY_COUNT].equals(COLUMN_PLAYS)
                && !columns[COLUMN_INDEX_PLAY_COUNT].equals(COLUMN_PLAY_COUNT)) {
            // older versions of the file use a different column name
            throw new FileStoreException("Column [" + COLUMN_PLAYS + "] or ["
                    + COLUMN_PLAY_COUNT + "] do not exist where it should exist.");
        }
        if (!columns[COLUMN_INDEX_TRACK_NUMBER].equals(COLUMN_TRACK_NUMBER)) {
            throw new FileStoreException("Column [" + COLUMN_TRACK_NUMBER
                    + "] does not exist where it should exist.");
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
        Integer trackNumber = null;
        if (columns[COLUMN_INDEX_TRACK_NUMBER].length() != 0) {
            trackNumber = Integer.parseInt(columns[COLUMN_INDEX_TRACK_NUMBER]);
        }
        return new StatisticsFromLine(new Song()
                .withArtistName(columns[COLUMN_INDEX_ARTIST_NAME])
                .withAlbumName(columns[COLUMN_INDEX_ALBUM_NAME])
                .withName(columns[COLUMN_INDEX_SONG_NAME])
                .withTrackNumber(trackNumber), new SongStatistics().withPlayCount(Integer
                .parseInt(columns[COLUMN_INDEX_PLAY_COUNT].length() == 0 ? "0"
                        : columns[COLUMN_INDEX_PLAY_COUNT])));
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

    @Override
    public final Map<String, Snapshot> getSnapshotsWithoutStatistics(List<String> snapshotNames) {
        throw new UnsupportedOperationException("Not Supported");
    }

    @Override
    public final Map<String, SongStatistics> getSongStatisticsFromSnapshots(
            String artistName,
            String albumName,
            String songName,
            List<String> snapshotNames) {
        throw new UnsupportedOperationException("Not Supported");
    }

    @Override
    public final void writeSnapshot(Snapshot snapshot) throws StoreException {
        throw new UnsupportedOperationException("Not Supported");

    }

    @Override
    public final void deleteAll() throws StoreException {
        throw new UnsupportedOperationException("Not Supported");
    }

}
