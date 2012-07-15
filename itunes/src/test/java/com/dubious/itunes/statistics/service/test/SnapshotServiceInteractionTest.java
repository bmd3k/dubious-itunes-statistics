package com.dubious.itunes.statistics.service.test;

import static org.mockito.Mockito.verify;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.SnapshotService;
import com.dubious.itunes.statistics.service.SnapshotServiceImpl;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.validate.SnapshotValidation;

/**
 * Interaction-level tests for {@link SnapshotService}.
 */
public class SnapshotServiceInteractionTest {

    private SnapshotService snapshotService;
    private SnapshotValidation snapshotValidation;
    private SnapshotStore snapshotStore;

    private DateTime yesterday = new DateTime().minusDays(1);

    /**
     * Test setup.
     */
    @Before
    public final void before() {
        snapshotStore = Mockito.mock(SnapshotStore.class);
        snapshotValidation = Mockito.mock(SnapshotValidation.class);
        snapshotService = new SnapshotServiceImpl(snapshotValidation, snapshotStore);
    }

    /**
     * Test basic write.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testWrite() throws StatisticsException {
        // exercise
        snapshotService.writeSnapshot(new Snapshot().withName("snapshot").withDate(yesterday));

        // verify
        verify(snapshotValidation).validateOnWrite(
                new Snapshot().withName("snapshot").withDate(yesterday));
        verify(snapshotStore).writeSnapshot(
                new Snapshot().withName("snapshot").withDate(yesterday));
    }

    /**
     * Test write with statistics data.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testWriteWithSongStatistics() throws StatisticsException {
        // exercise
        snapshotService.writeSnapshot(new Snapshot()
                .withName("snapshot")
                .withDate(yesterday)
                .addStatistic(
                        new Song()
                                .withArtistName("artist")
                                .withAlbumName("album")
                                .withName("song")
                                .withTrackNumber(5),
                        new SongStatistics().withPlayCount(5)));

        // verify
        verify(snapshotValidation).validateOnWrite(
                new Snapshot()
                        .withName("snapshot")
                        .withDate(yesterday)
                        .addStatistic(
                                new Song()
                                        .withArtistName("artist")
                                        .withAlbumName("album")
                                        .withName("song")
                                        .withTrackNumber(5),
                                new SongStatistics().withPlayCount(5)));
        verify(snapshotStore).writeSnapshot(
                new Snapshot()
                        .withName("snapshot")
                        .withDate(yesterday)
                        .addStatistic(
                                new Song()
                                        .withArtistName("artist")
                                        .withAlbumName("album")
                                        .withName("song")
                                        .withTrackNumber(5),
                                new SongStatistics().withPlayCount(5)));
    }
}
