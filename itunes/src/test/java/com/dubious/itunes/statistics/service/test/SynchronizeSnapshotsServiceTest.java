package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.SnapshotSynchronizeService;
import com.dubious.itunes.statistics.store.SnapshotStore;

/**
 * Tests for {@link SnapshotSynchronizeService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SynchronizeSnapshotsServiceTest {

    @Resource(name = "testSourceMongoDbSnapshotStore")
    private SnapshotStore sourceSnapshotStore;
    @Resource(name = "mongoDbSnapshotStore")
    private SnapshotStore targetSnapshotStore;
    @Resource(name = "testSnapshotSynchronizeService")
    private SnapshotSynchronizeService snapshotSynchronizeService;

    private DateTime today = new DateTime();
    private DateTime yesterday = today.minusDays(1);
    private DateTime tomorrow = today.plusDays(1);

    //@formatter:off
    private Snapshot snapshot1 = new Snapshot()
            .withName("Snapshot1")
            .withDate(yesterday)
            .addStatistic(new Song()
                    .withArtistName("artist1")
                    .withAlbumName("album1")
                    .withName("song1"),
                    new SongStatistics().withPlayCount(3));
    private Snapshot snapshot2 = new Snapshot()
            .withName("Snapshot2")
            .withDate(today)
            .addStatistic(new Song()
                    .withArtistName("artist1")
                    .withAlbumName("album1")
                    .withName("song1"),
                    new SongStatistics().withPlayCount(3))
            .addStatistic(new Song()
                    .withArtistName("artist2")
                    .withAlbumName("album2")
                    .withName("song2"),
                    new SongStatistics().withPlayCount(3))
            .addStatistic(new Song()
                    .withArtistName("artist3")
                    .withAlbumName("album3")
                    .withName("song3"),
                    new SongStatistics().withPlayCount(3));
    private Snapshot snapshot3 = new Snapshot()
            .withName("Snapshot3")
            .withDate(tomorrow)
            .addStatistic(new Song()
                    .withArtistName("artist3")
                    .withAlbumName("album3")
                    .withName("song3"),
                    new SongStatistics().withPlayCount(3));
    //@formatter:on

    /**
     * Test synchronization of no snapshots.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSynchronizeNoSnapshots() throws StatisticsException {
        snapshotSynchronizeService.synchronizeSnapshots();
        assertEquals(0, targetSnapshotStore.getSnapshots().size());
    }

    /**
     * Test synchronization of one snapshot.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSynchronizeOneSnapshot() throws StatisticsException {
        sourceSnapshotStore.writeSnapshot(snapshot1);
        snapshotSynchronizeService.synchronizeSnapshots();
        assertEquals(asList(snapshot1), targetSnapshotStore.getSnapshots());
    }

    /**
     * Test synchronization of multiple snapshots.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSynchronizeMultipleSnapshots() throws StatisticsException {
        sourceSnapshotStore.writeSnapshot(snapshot1);
        sourceSnapshotStore.writeSnapshot(snapshot3);
        sourceSnapshotStore.writeSnapshot(snapshot2);
        snapshotSynchronizeService.synchronizeSnapshots();
        assertEquals(asList(snapshot1, snapshot2, snapshot3), targetSnapshotStore.getSnapshots());
    }

    /**
     * Test multiple calls to synchronizations interlaced with writes of new snapshots.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testSynchronizeMultipleTimes() throws StatisticsException {
        sourceSnapshotStore.writeSnapshot(snapshot1);
        sourceSnapshotStore.writeSnapshot(snapshot2);
        snapshotSynchronizeService.synchronizeSnapshots();
        assertEquals(asList(snapshot1, snapshot2), targetSnapshotStore.getSnapshots());

        sourceSnapshotStore.writeSnapshot(snapshot3);
        snapshotSynchronizeService.synchronizeSnapshots();
        assertEquals(asList(snapshot1, snapshot2, snapshot3), targetSnapshotStore.getSnapshots());

        snapshotSynchronizeService.synchronizeSnapshots();
        assertEquals(asList(snapshot1, snapshot2, snapshot3), targetSnapshotStore.getSnapshots());
    }

    /**
     * Test cleanup.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @After
    public final void after() throws StatisticsException {
        sourceSnapshotStore.deleteAll();
        targetSnapshotStore.deleteAll();
    }
}
