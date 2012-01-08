package com.dubious.itunes.statistics.service.test;

import static java.util.Arrays.asList;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.apache.commons.io.FileUtils.readLines;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.service.AnalysisService;
import com.dubious.itunes.statistics.service.AnalysisServiceImpl;
import com.dubious.itunes.statistics.service.HistoryService;
import com.dubious.itunes.statistics.service.HistoryServiceImpl;
import com.dubious.itunes.statistics.store.ReadOnlySnapshotStore;
import com.dubious.itunes.statistics.store.file.FileSnapshotStore;
import com.dubious.itunes.statistics.store.file.FileStoreProperties;

/**
 * Tests for the {@link AnalysisService}.
 */
public class AnalysisServiceTest {

    private static FileStoreProperties fileStoreProperties;
    private static ReadOnlySnapshotStore snapshotFileStore;
    private static HistoryService snapshotService;
    private static AnalysisService analysisService;

    /**
     * Setup at the class level.
     */
    @BeforeClass
    public static void beforeClass() {
        fileStoreProperties = new FileStoreProperties("test_files", "UTF-16");
        snapshotFileStore = new FileSnapshotStore(fileStoreProperties);
        snapshotService = new HistoryServiceImpl(snapshotFileStore);
        analysisService = new AnalysisServiceImpl();
    }

    /**
     * Setup for a test.
     * 
     * @throws IOException On unexpected error.
     */
    @Before
    public final void before() throws IOException {
        deleteDirectory(new File("test_files/output"));
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
        SnapshotsHistory history =
                snapshotService.compareSnapshots(asList("101201 - Music.txt",
                        "111201 - Music.txt"));
        analysisService.writeAnalysis(history, "test_files/output/output.txt");

        assertEquals(readLines(new File(
                "test_files/AnalysisServiceTest/testWriteAnalysisWithTwoSnapshots_expected.txt"),
                "UTF-8"),
                readLines(new File("test_files/output/output.txt"), "UTF-8"));
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
        SnapshotsHistory history =
                snapshotService.compareSnapshots(asList("101130 - Music.txt",
                        "111201 - Music.txt",
                        "111205 - Music.txt",
                        "111206 - Music.txt"));
        analysisService.writeAnalysis(history, "test_files/output/output.txt");

        assertEquals(readLines(new File(
                "test_files/AnalysisServiceTest/testWriteAnalysisWithManySnapshots_expected.txt"),
                "UTF-8"),
                readLines(new File("test_files/output/output.txt"), "UTF-8"));
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
        SnapshotsHistory history =
                snapshotService.compareSnapshots(asList("101130 - Music.txt",
                        "111201 - Music.txt",
                        "111205 - Music.txt",
                        "111206 - Music.txt"));
        analysisService.writeAnalysisOrderByDifference(history, "test_files/output/output.txt");

        assertEquals(readLines(new File(
                "test_files/AnalysisServiceTest/testWriteAnalysisOrderByDifference_expected.txt"),
                "UTF-8"),
                readLines(new File("test_files/output/output.txt"), "UTF-8"));
    }
}
