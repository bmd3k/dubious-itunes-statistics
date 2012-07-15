package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.service.SnapshotService;
import com.dubious.itunes.statistics.service.SnapshotSynchronizationAdjustment;
import com.dubious.itunes.statistics.service.SnapshotSynchronizeService;
import com.dubious.itunes.statistics.service.SnapshotSynchronizeServiceImpl;
import com.dubious.itunes.statistics.store.SnapshotStore;

/**
 * Tests for {@link SnapshotSynchronizeService}.
 */
public class SynchronizeSnapshotsServiceInteractionTest {

    private SnapshotStore sourceSnapshotStore;
    private SnapshotStore targetSnapshotStore;
    private SnapshotService targetSnapshotService;
    private SnapshotSynchronizationAdjustment adjustor;
    private SnapshotSynchronizeService snapshotSynchronizeService;

    /**
     * Test setup.
     */
    @Before
    public final void before() {
        sourceSnapshotStore = mock(SnapshotStore.class);
        targetSnapshotStore = mock(SnapshotStore.class);
        targetSnapshotService = mock(SnapshotService.class);
        adjustor = mock(SnapshotSynchronizationAdjustment.class);
        snapshotSynchronizeService =
                new SnapshotSynchronizeServiceImpl(
                        sourceSnapshotStore,
                        targetSnapshotStore,
                        targetSnapshotService,
                        adjustor);
    }

    /**
     * Ensure snapshots are properly adjusted before being sent to target store.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSnapshotAdjustment() throws StatisticsException {
        // define interactions
        when(sourceSnapshotStore.getSnapshotsWithoutStatistics()).thenReturn(
                asList(new Snapshot().withName("snapshot")));
        when(targetSnapshotStore.getSnapshotsWithoutStatistics(asList("snapshot"))).thenReturn(
                new HashMap<String, Snapshot>());
        when(sourceSnapshotStore.getSnapshot("snapshot")).thenReturn(
                new Snapshot().withName("snapshot").addStatistic(
                        new Song().withArtistName("artist").withName("song").withTrackNumber(5),
                        null));
        // this is part definition of interaction and part verification
        when(
                adjustor.adjustSnapshotForSynchronization(new Snapshot()
                        .withName("snapshot")
                        .addStatistic(
                                new Song()
                                        .withArtistName("artist")
                                        .withName("song")
                                        .withTrackNumber(5),
                                null))).thenReturn(
                new Snapshot().withName("snapshot").addStatistic(
                        new Song().withArtistName("artist").withName("song"),
                        null));

        // exercise
        snapshotSynchronizeService.synchronizeSnapshots();

        // verify
        // ensure object returned by the adjustor is written to the target snapshot store
        verify(targetSnapshotService).writeSnapshot(
                new Snapshot().withName("snapshot").addStatistic(
                        new Song().withArtistName("artist").withName("song"),
                        null));

    }
}
