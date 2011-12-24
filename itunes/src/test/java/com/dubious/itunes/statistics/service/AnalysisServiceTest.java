package com.dubious.itunes.statistics.service;

import static junit.framework.Assert.assertTrue;
import static org.apache.commons.io.FileUtils.contentEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dubious.itunes.model.StatisticsException;
import com.dubious.itunes.statistics.model.SnapshotsHistory;
import com.dubious.itunes.statistics.store.ReadOnlySnapshotStore;
import com.dubious.itunes.statistics.store.file.FileStoreProperties;
import com.dubious.itunes.statistics.store.file.FileSnapshotStore;

public class AnalysisServiceTest {

    private static FileStoreProperties fileStoreProperties;
    private static ReadOnlySnapshotStore snapshotFileStore;
    private static SnapshotService snapshotService;
    private static AnalysisService analysisService;

    @BeforeClass
    public static void beforeClass() {
        fileStoreProperties = new FileStoreProperties("test_files", "UTF-16");
        snapshotFileStore = new FileSnapshotStore(fileStoreProperties);
        snapshotService = new SnapshotServiceImpl(snapshotFileStore);
        analysisService = new AnalysisServiceImpl();
    }

    @Before
    public void before() throws IOException {
        FileUtils.deleteDirectory(new File("test_files/output"));
    }

    @Test
    public void testWriteAnalysis() throws StatisticsException, IOException {
        SnapshotsHistory history =
                snapshotService.compareSnapshots("101201 - Music.txt", "111201 - Music.txt");
        analysisService.writeAnalysis(history, "test_files/output/output.txt");

        assertTrue(contentEquals(new File(
                "test_files/testAnalysisService/testWriteAnalysis_expected.txt"), new File(
                "test_files/output/output.txt")));
    }

    @Test
    public void testWriteAnalysisOrderByDifference() throws StatisticsException, IOException {
        SnapshotsHistory history =
                snapshotService.compareSnapshots("101201 - Music.txt", "111201 - Music.txt");
        analysisService.writeAnalysisOrderByDifference(history, "test_files/output/output.txt");

        assertTrue(contentEquals(new File(
                "test_files/testAnalysisService/testWriteAnalysisOrderByDifference_expected.txt"),
                new File("test_files/output/output.txt")));
    }
}
