package com.dubious.itunes.statistics.validate;

import java.util.Map;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.SnapshotArtistNameNotSpecifiedException;
import com.dubious.itunes.statistics.exception.SnapshotDateNotSpecifiedException;
import com.dubious.itunes.statistics.exception.SnapshotDifferenceSpecifiedException;
import com.dubious.itunes.statistics.exception.SnapshotNameNotSpecifiedException;
import com.dubious.itunes.statistics.exception.SnapshotPlayCountNotSpecifiedException;
import com.dubious.itunes.statistics.exception.SnapshotSongNameNotSpecifiedException;
import com.dubious.itunes.statistics.exception.SnapshotSongNotSpecifiedException;
import com.dubious.itunes.statistics.exception.SnapshotSongStatisticsNotSpecifiedException;
import com.dubious.itunes.statistics.exception.SnapshotTrackNumberImproperException;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;

/**
 * Implementation of snapshot validation.
 */
public class SnapshotValidator implements SnapshotValidation {

    @Override
    public final void validateOnWrite(Snapshot snapshot) throws StatisticsException {
        if (snapshot.getName() == null || snapshot.getName().length() == 0) {
            throw new SnapshotNameNotSpecifiedException();
        }

        if (snapshot.getDate() == null) {
            throw new SnapshotDateNotSpecifiedException(snapshot.getName());
        }

        for (Map.Entry<Song, SongStatistics> statistics : snapshot.getStatistics().entrySet()) {
            validateSongStatisticsOnWrite(snapshot, statistics.getKey(), statistics.getValue());
        }
    }

    /**
     * Validate snapshot song and statistics data on write.
     * 
     * @param snapshot The snapshot being validated.
     * @param song The song to validate.
     * @param songStatistics The statistics to validate.
     * @throws StatisticsException If any of the checks fail.
     */
    private void validateSongStatisticsOnWrite(
            Snapshot snapshot,
            Song song,
            SongStatistics songStatistics) throws StatisticsException {
        if (song == null) {
            throw new SnapshotSongNotSpecifiedException(snapshot.getName());
        }

        if (song.getArtistName() == null || song.getArtistName().length() == 0) {
            throw new SnapshotArtistNameNotSpecifiedException(snapshot.getName());
        }

        if (song.getName() == null || song.getName().length() == 0) {
            throw new SnapshotSongNameNotSpecifiedException(
                    snapshot.getName(),
                    song.getArtistName(),
                    song.getAlbumName());
        }

        if (song.getAlbumName() == null || song.getAlbumName().length() == 0) {
            if (song.getTrackNumber() != null) {
                throw new SnapshotTrackNumberImproperException(
                        snapshot.getName(),
                        song.getArtistName(),
                        song.getName());
            }
        }

        if (songStatistics == null) {
            throw new SnapshotSongStatisticsNotSpecifiedException(
                    snapshot.getName(),
                    song.getArtistName(),
                    song.getAlbumName(),
                    song.getName());
        }

        if (songStatistics.getPlayCount() == null) {
            throw new SnapshotPlayCountNotSpecifiedException(
                    snapshot.getName(),
                    song.getArtistName(),
                    song.getAlbumName(),
                    song.getName());
        }

        if (songStatistics.getDifference() != null) {
            throw new SnapshotDifferenceSpecifiedException(
                    snapshot.getName(),
                    song.getArtistName(),
                    song.getAlbumName(),
                    song.getName());
        }
    }

}
