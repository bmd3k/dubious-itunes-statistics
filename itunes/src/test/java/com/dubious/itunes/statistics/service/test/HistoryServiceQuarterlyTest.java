package com.dubious.itunes.statistics.service.test;

import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Test;

import com.dubious.itunes.statistics.service.HistoryService;
import com.dubious.itunes.statistics.store.SnapshotStore;

/**
 * Tests the {@link HistoryService#getQuarterlySnapshots()} method.
 */
public class HistoryServiceQuarterlyTest {

    @Resource(name = "mongoDbSnapshotStore")
    private SnapshotStore snapshotStore;
    @Resource(name = "historyService")
    private HistoryService historyService;

    @Test
    public final void testWithNoSnapshots() {
        fail("Not Yet Implemented");
    }

    @Test
    public final void testWithSnapshots() {
        fail("Not Yet Implemented");
    }

}
