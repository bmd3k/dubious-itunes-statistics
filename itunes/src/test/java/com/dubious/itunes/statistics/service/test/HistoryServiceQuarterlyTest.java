package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import javax.annotation.Resource;

import org.joda.time.DateMidnight;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.service.HistoryService;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.StoreException;

/**
 * Tests the {@link HistoryService#getQuarterlySnapshots()} method.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/dubious/itunes/test/itunes-test-context.xml" })
public class HistoryServiceQuarterlyTest {

    @Resource(name = "mongoDbSnapshotStore")
    private SnapshotStore snapshotStore;
    @Resource(name = "historyService")
    private HistoryService historyService;

    // TODO: Need to ensure the Store-layer returns objects without history

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
     * Return result when there are no snapshots in the store.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testWithNoSnapshots() throws StatisticsException {
        assertEquals(Collections.<String>emptyList(), historyService.getQuarterlySnapshots());
    }

    /**
     * Return result when there are snapshots in the store.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testWithSnapshots() throws StatisticsException {
        snapshotStore.writeSnapshot(new Snapshot().withName("S1").withDate(
                new DateMidnight(1980, 5, 23).toDateTime()));
        snapshotStore.writeSnapshot(new Snapshot().withName("S2").withDate(
                new DateMidnight(1980, 5, 26).toDateTime()));
        snapshotStore.writeSnapshot(new Snapshot().withName("S3").withDate(
                new DateMidnight(1980, 5, 21).toDateTime()));
        snapshotStore.writeSnapshot(new Snapshot().withName("S4").withDate(
                new DateMidnight(1980, 7, 23).toDateTime()));
        snapshotStore.writeSnapshot(new Snapshot().withName("S5").withDate(
                new DateMidnight(1980, 7, 28).toDateTime()));

        assertEquals(asList("S2", "S5"), historyService.getQuarterlySnapshots());
    }

}
