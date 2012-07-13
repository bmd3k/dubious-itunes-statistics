package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;

import javax.annotation.Resource;

import org.joda.time.DateMidnight;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.SnapshotForQuarterNotFoundException;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.Quarter;
import com.dubious.itunes.statistics.service.SnapshotFilter;
import com.dubious.itunes.statistics.service.YearAndQuarter;

/**
 * Tests of {@link SnapshotFilter}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/dubious/itunes/test/itunes-test-context.xml" })
public class SnapshotFilterTest {

    @Resource(name = "snapshotFilter")
    private SnapshotFilter snapshotFilter;

    private Snapshot snapshot790522 = snapshot(1979, 5, 22);
    private Snapshot snapshot790523 = snapshot(1979, 5, 23);
    private Snapshot snapshot790808 = snapshot(1979, 8, 8);
    private Snapshot snapshot791126 = snapshot(1979, 11, 26);
    private Snapshot snapshot791210 = snapshot(1979, 12, 10);
    private Snapshot snapshot791224 = snapshot(1979, 12, 24);
    private Snapshot snapshot791225 = snapshot(1979, 12, 25);
    private Snapshot snapshot800101 = snapshot(1980, 1, 1);
    private Snapshot snapshot800315 = snapshot(1980, 3, 15);
    private Snapshot snapshot800522 = snapshot(1980, 5, 22);
    private Snapshot snapshot800523 = snapshot(1980, 5, 23);
    private Snapshot snapshot800623 = snapshot(1980, 6, 23);

    /**
     * Create a snapshot for testing purposes, passing the date of the snapshot.
     * 
     * @param year The year of the snapshot.
     * @param month The month of the snapshot.
     * @param day The day of the snapshot.
     * @return The snapshot.
     */
    private Snapshot snapshot(int year, int month, int day) {
        return new Snapshot().withName("s").withDate(
                new DateMidnight(year, month, day).toDateTime());
    }

    /**
     * Test attempt to filter no snapshots.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testWithNoSnapshots() throws StatisticsException {
        assertEquals(
                Collections.<Snapshot>emptyList(),
                snapshotFilter.filterByLastInQuarter(Collections.<Snapshot>emptyList()));
    }

    /**
     * Test filter of one snapshot.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testWithOneSnapshot() throws StatisticsException {
        assertEquals(
                asList(snapshot800101),
                snapshotFilter.filterByLastInQuarter(asList(snapshot800101)));
    }

    /**
     * Test that the snapshots returned in the filtered list contain all the original information in
     * a snapshot.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testEntireSnapshotReturned() throws StatisticsException {
        assertEquals(
                asList(new Snapshot()
                        .withName("Snapshot")
                        .withDate(new DateMidnight().toDateTime())
                        .addStatistic(
                                new Song()
                                        .withArtistName("artist")
                                        .withAlbumName("album")
                                        .withName("song"),
                                new SongStatistics().withPlayCount(12))),
                snapshotFilter.filterByLastInQuarter(asList(new Snapshot()
                        .withName("Snapshot")
                        .withDate(new DateMidnight().toDateTime())
                        .addStatistic(
                                new Song()
                                        .withArtistName("artist")
                                        .withAlbumName("album")
                                        .withName("song"),
                                new SongStatistics().withPlayCount(12)))));
    }

    /**
     * Test filtering multiple snapshots in different quarters. In fact none of the snapshots should
     * be filtered out.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testWithMultipleSnapshotsDifferentQuarters() throws StatisticsException {
        assertEquals(
                asList(snapshot790808, snapshot791210, snapshot800315),
                snapshotFilter.filterByLastInQuarter(asList(
                        snapshot790808,
                        snapshot791210,
                        snapshot800315)));
    }

    /**
     * Test filtering multiple snapshots in the same quarter. All but one of the snapshots should be
     * filtered out.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testWithMultipleSnapshotsInSameQuarter() throws StatisticsException {
        assertEquals(asList(snapshot800623), snapshotFilter.filterByLastInQuarter(asList(
                snapshot800522,
                snapshot800523,
                snapshot800623)));
    }

    /**
     * Test complex multi-quarter filtering.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testWithMultipleSnapshotsMultipleYears() throws StatisticsException {
        assertEquals(
                asList(
                        snapshot790523,
                        snapshot790808,
                        snapshot791225,
                        snapshot800315,
                        snapshot800623),
                snapshotFilter.filterByLastInQuarter(asList(
                        snapshot790522,
                        snapshot790523,
                        snapshot790808,
                        snapshot791126,
                        snapshot791210,
                        snapshot791224,
                        snapshot791225,
                        snapshot800101,
                        snapshot800315,
                        snapshot800522,
                        snapshot800523,
                        snapshot800623)));
    }

    /**
     * Test filtering when the input is not properly sorted.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testWithMultipleSnapshotsOutOfOrder() throws StatisticsException {
        assertEquals(
                asList(
                        snapshot790523,
                        snapshot790808,
                        snapshot791225,
                        snapshot800315,
                        snapshot800623),
                snapshotFilter.filterByLastInQuarter(asList(
                        snapshot790808,
                        snapshot791126,
                        snapshot800315,
                        snapshot800623,
                        snapshot800522,
                        snapshot791224,
                        snapshot791225,
                        snapshot800101,
                        snapshot800523,
                        snapshot791210,
                        snapshot790522,
                        snapshot790523)));
    }

    /**
     * Test attempt to filter but one quarter exists without snapshot.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Test
    public final void testMissingSnapshotForQuarter() throws StatisticsException {
        try {
            snapshotFilter.filterByLastInQuarter(asList(
                    snapshot791210,
                    snapshot791225,
                    snapshot800623));
            fail("expected exception not thrown");
        } catch (SnapshotForQuarterNotFoundException e) {
            assertEquals(new SnapshotForQuarterNotFoundException(new YearAndQuarter(
                    1980,
                    Quarter.Q1)), e);
        }
    }
}
