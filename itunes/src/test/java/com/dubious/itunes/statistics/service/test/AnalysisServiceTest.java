package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.apache.commons.io.FileUtils.readLines;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.service.AnalysisService;
import com.dubious.itunes.statistics.service.HistoryService;
import com.dubious.itunes.statistics.store.SnapshotStore;

/**
 * Tests for the {@link AnalysisService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/dubious/itunes/test/itunes-test-context.xml" })
public class AnalysisServiceTest {

    @Resource(name = "mongoDbSnapshotStore")
    private SnapshotStore snapshotStore;
    @Resource(name = "historyService")
    private HistoryService historyService;
    @Resource(name = "analysisService")
    private AnalysisService analysisService;

    private String snapshot1 = "snapshot1";
    private String snapshot2 = "snapshot2";
    private String snapshot3 = "snapshot3";
    private String snapshot4 = "snapshot4";
    private DateTime december1 = new DateMidnight(2011, 12, 1).toDateTime();
    private DateTime december5 = new DateMidnight(2011, 12, 5).toDateTime();
    private DateTime december10 = new DateMidnight(2011, 12, 10).toDateTime();
    private DateTime december12 = new DateMidnight(2011, 12, 12).toDateTime();
    private Song song1 = new Song()
            .withArtistName("Arctic Monkeys")
            .withAlbumName("Whatever People Say I Am, That's What I'm Not")
            .withName("Mardy Bum");
    private Song song2 = new Song()
            .withArtistName("Nada Surf")
            .withAlbumName("The Weight is a Gift")
            .withName("Blankest Year");
    private Song song3 = new Song()
            .withArtistName("Death From Above 1979")
            .withAlbumName("You're A Woman I'm A Machine")
            .withName("Going Steady");

    /**
     * Setup of tests.
     * 
     * @throws StatisticsException On unexpected error.
     */
    @Before
    public final void before() throws StatisticsException {
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot1)
                .withDate(december1)
                .addStatistic(song1, new SongStatistics().withPlayCount(10))
                .addStatistic(song2, new SongStatistics().withPlayCount(8)));
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot2)
                .withDate(december5)
                .addStatistic(song1, new SongStatistics().withPlayCount(15))
                .addStatistic(song2, new SongStatistics().withPlayCount(8))
                .addStatistic(song3, new SongStatistics().withPlayCount(2)));
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot3)
                .withDate(december10)
                .addStatistic(song2, new SongStatistics().withPlayCount(17))
                .addStatistic(song1, new SongStatistics().withPlayCount(15))
                .addStatistic(song3, new SongStatistics().withPlayCount(10)));
        snapshotStore.writeSnapshot(new Snapshot()
                .withName(snapshot4)
                .withDate(december12)
                .addStatistic(song2, new SongStatistics().withPlayCount(19))
                .addStatistic(song1, new SongStatistics().withPlayCount(16))
                .addStatistic(song3, new SongStatistics().withPlayCount(12)));
    }

    /**
     * Tear down for a test.
     * 
     * @throws StatisticsException On unexpected error.
     * @throws IOException On unexpected error.
     */
    @After
    public final void after() throws StatisticsException, IOException {
        deleteDirectory(new File("test_files/output"));
        snapshotStore.deleteAll();
    }

    /**
     * Test the ability to write an analysis of history of two snapshots with default order.
     * 
     * @throws StatisticsException On unexpected error.
     * @throws IOException On unexpected error.
     */
    @Test
    public final void testWriteAnalysisWithTwoSnapshots() throws StatisticsException,
            IOException {
        analysisService.writeAnalysis(
                historyService.generateSnapshotHistory(asList(snapshot3, snapshot4)),
                "test_files/output/output.txt");

        //@formatter:off
        assertEquals(readLines(new File(
                "test_files/AnalysisServiceTest/testWriteAnalysisWithTwoSnapshots_expected.txt"),
                "UTF-8"),
                readLines(new File("test_files/output/output.txt"), "UTF-8"));
        //@formatter:on
    }

    /**
     * Test the ability to write an analysis of history of many snapshots with default order.
     * 
     * @throws StatisticsException On unexpected error.
     * @throws IOException On unexpected error.
     */
    @Test
    public final void testWriteAnalysisWithManySnapshots() throws StatisticsException,
            IOException {
        analysisService.writeAnalysis(historyService.generateSnapshotHistory(asList(
                snapshot1,
                snapshot2,
                snapshot3,
                snapshot4)), "test_files/output/output.txt");

        //@formatter:off
        assertEquals(readLines(new File(
                "test_files/AnalysisServiceTest/testWriteAnalysisWithManySnapshots_expected.txt"),
                "UTF-8"),
                readLines(new File("test_files/output/output.txt"), "UTF-8"));
        //@formatter:on
    }

    /**
     * Test the ability to write an analysis, ordered by the difference between the earliest and
     * latest snapshot.
     * 
     * @throws StatisticsException On unexpected error.
     * @throws IOException On unexpected error.
     */
    @Test
    public final void testWriteAnalysisOrderByDifference() throws StatisticsException,
            IOException {
        analysisService.writeAnalysisOrderByDifference(
                historyService.generateSnapshotHistory(asList(
                        snapshot1,
                        snapshot2,
                        snapshot3,
                        snapshot4)),
                "test_files/output/output.txt");

        //@formatter:off
        assertEquals(readLines(new File(
                "test_files/AnalysisServiceTest/testWriteAnalysisOrderByDifference_expected.txt"),
                "UTF-8"),
                readLines(new File("test_files/output/output.txt"), "UTF-8"));
        //@formatter:on
    }
}
