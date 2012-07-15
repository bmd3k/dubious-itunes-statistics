package com.dubious.itunes.statistics.service.test;

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
import com.dubious.itunes.statistics.service.SnapshotService;
import com.dubious.itunes.statistics.service.SongService;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.StoreException;

/**
 * System-level tests for {@link SnapshotService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/dubious/itunes/test/itunes-test-context.xml" })
public class SnapshotServiceTest {

    @Resource(name = "snapshotService")
    private SnapshotService snapshotService;
    @Resource(name = "mongoDbSnapshotStore")
    private SnapshotStore snapshotStore;
    @Resource(name = "songService")
    private SongService songService;

    /**
     * Tear down tests.
     * 
     * @throws StoreException On unexpected error.
     */
    @After
    public final void after() throws StoreException {
        snapshotStore.deleteAll();
    }

    /**
     * Test write of snapshot.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testWriteSnapshot() throws StatisticsException {
        snapshotService.writeSnapshot(new Snapshot()
                .withName("snapshot")
                .withDate(new DateTime())
                .addStatistic(
                        new Song()
                                .withArtistName("artist")
                                .withAlbumName("album")
                                .withName("song"),
                        new SongStatistics().withPlayCount(12)));

        assertEquals(1, songService.getSongsForAlbum("artist", "album").size());
    }
}
