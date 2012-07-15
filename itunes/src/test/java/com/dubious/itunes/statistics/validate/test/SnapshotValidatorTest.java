package com.dubious.itunes.statistics.validate.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

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
import com.dubious.itunes.statistics.validate.SnapshotValidator;

/**
 * Tests of {@link SnapshotValidator}.
 */
public class SnapshotValidatorTest {

    private SnapshotValidator snapshotValidator;

    private DateTime yesterday = new DateTime().minusDays(1);

    /**
     * Test setup.
     */
    @Before
    public final void before() {
        snapshotValidator = new SnapshotValidator();
    }

    /**
     * Snapshot name not specified.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSnapshotNameNotSpecified() throws StatisticsException {
        try {
            snapshotValidator.validateOnWrite(new Snapshot());
            fail("expected exception not thrown");
        } catch (SnapshotNameNotSpecifiedException e) {
            assertEquals(new SnapshotNameNotSpecifiedException(), e);
        }

        try {
            snapshotValidator.validateOnWrite(new Snapshot().withName(""));
            fail("expected exception not thrown");
        } catch (SnapshotNameNotSpecifiedException e) {
            assertEquals(new SnapshotNameNotSpecifiedException(), e);
        }
    }

    /**
     * Date not specified.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testDateNotSpecified() throws StatisticsException {
        try {
            snapshotValidator.validateOnWrite(new Snapshot().withName("snapshot"));
            fail("expected exception not thrown");
        } catch (SnapshotDateNotSpecifiedException e) {
            assertEquals(new SnapshotDateNotSpecifiedException("snapshot"), e);
        }
    }

    /**
     * Song is null.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSongNull() throws StatisticsException {
        try {
            snapshotValidator.validateOnWrite(new Snapshot()
                    .withName("snapshot")
                    .withDate(yesterday)
                    .addStatistic(null, null));
            fail("expected exception not thrown");
        } catch (SnapshotSongNotSpecifiedException e) {
            assertEquals(new SnapshotSongNotSpecifiedException("snapshot"), e);
        }
    }

    /**
     * Artist name not specified.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testArtistNameNotSpecified() throws StatisticsException {
        try {
            snapshotValidator.validateOnWrite(new Snapshot()
                    .withName("snapshot")
                    .withDate(yesterday)
                    .addStatistic(new Song(), null));
            fail("expected exception not thrown");
        } catch (SnapshotArtistNameNotSpecifiedException e) {
            assertEquals(new SnapshotArtistNameNotSpecifiedException("snapshot"), e);
        }

        try {
            snapshotValidator.validateOnWrite(new Snapshot()
                    .withName("snapshot")
                    .withDate(yesterday)
                    .addStatistic(new Song().withArtistName(""), null));
            fail("expected exception not thrown");
        } catch (SnapshotArtistNameNotSpecifiedException e) {
            assertEquals(new SnapshotArtistNameNotSpecifiedException("snapshot"), e);
        }
    }

    /**
     * Song name is not specified.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSongNameNotSpecified() throws StatisticsException {
        try {
            // exercise
            snapshotValidator.validateOnWrite(new Snapshot()
                    .withName("snapshot")
                    .withDate(yesterday)
                    .addStatistic(new Song().withArtistName("artist"), null));
            fail("expected exception not thrown");
        } catch (SnapshotSongNameNotSpecifiedException e) {
            assertEquals(
                    new SnapshotSongNameNotSpecifiedException("snapshot", "artist", null),
                    e);
        }

        try {
            // exercise
            snapshotValidator.validateOnWrite(new Snapshot()
                    .withName("snapshot")
                    .withDate(yesterday)
                    .addStatistic(
                            new Song()
                                    .withArtistName("artist")
                                    .withAlbumName("album")
                                    .withName(""),
                            null));
            fail("expected exception not thrown");
        } catch (SnapshotSongNameNotSpecifiedException e) {
            assertEquals(
                    new SnapshotSongNameNotSpecifiedException("snapshot", "artist", "album"),
                    e);
        }
    }

    /**
     * Album name is not specified but a track number is specified.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testAlbumNameNotSpecifiedButTrackNumberExists() throws StatisticsException {
        try {
            // exercise
            snapshotValidator.validateOnWrite(new Snapshot()
                    .withName("snapshot")
                    .withDate(yesterday)
                    .addStatistic(
                            new Song()
                                    .withArtistName("artist")
                                    .withName("song")
                                    .withTrackNumber(10),
                            null));
            fail("expected exception not thrown");
        } catch (SnapshotTrackNumberImproperException e) {
            assertEquals(
                    new SnapshotTrackNumberImproperException("snapshot", "artist", "song"),
                    e);
        }

        try {
            // exercise
            snapshotValidator.validateOnWrite(new Snapshot()
                    .withName("snapshot")
                    .withDate(yesterday)
                    .addStatistic(
                            new Song()
                                    .withArtistName("artist")
                                    .withAlbumName("")
                                    .withName("song")
                                    .withTrackNumber(10),
                            null));
            fail("expected exception not thrown");
        } catch (SnapshotTrackNumberImproperException e) {
            assertEquals(
                    new SnapshotTrackNumberImproperException("snapshot", "artist", "song"),
                    e);
        }
    }

    /**
     * Song statistics are null.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSongStatisticsNull() throws StatisticsException {
        try {
            // exercise
            snapshotValidator.validateOnWrite(new Snapshot()
                    .withName("snapshot")
                    .withDate(yesterday)
                    .addStatistic(
                            new Song()
                                    .withArtistName("artist")
                                    .withAlbumName("album")
                                    .withName("song"),
                            null));
            fail("expected exception not thrown");
        } catch (SnapshotSongStatisticsNotSpecifiedException e) {
            assertEquals(new SnapshotSongStatisticsNotSpecifiedException(
                    "snapshot",
                    "artist",
                    "album",
                    "song"), e);
        }
    }

    /**
     * Play count not specified.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testPlayCountNotSpecified() throws StatisticsException {
        try {
            // exercise
            snapshotValidator.validateOnWrite(new Snapshot()
                    .withName("snapshot")
                    .withDate(yesterday)
                    .addStatistic(
                            new Song()
                                    .withArtistName("artist")
                                    .withAlbumName("album")
                                    .withName("song"),
                            new SongStatistics()));
            fail("expected exception not thrown");
        } catch (SnapshotPlayCountNotSpecifiedException e) {
            assertEquals(new SnapshotPlayCountNotSpecifiedException(
                    "snapshot",
                    "artist",
                    "album",
                    "song"), e);
        }
    }

    /**
     * Difference is specified.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testDifferenceSpecified() throws StatisticsException {
        try {
            // exercise
            snapshotValidator.validateOnWrite(new Snapshot()
                    .withName("snapshot")
                    .withDate(yesterday)
                    .addStatistic(
                            new Song()
                                    .withArtistName("artist")
                                    .withAlbumName("album")
                                    .withName("song"),
                            new SongStatistics().withPlayCount(5).withDifference(5)));
            fail("expected exception not thrown");
        } catch (SnapshotDifferenceSpecifiedException e) {
            assertEquals(new SnapshotDifferenceSpecifiedException(
                    "snapshot",
                    "artist",
                    "album",
                    "song"), e);
        }
    }
}
