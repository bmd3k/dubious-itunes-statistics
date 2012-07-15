package com.dubious.itunes.statistics.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.store.SnapshotStore;

/**
 * Implementation of snapshot synchronization service.
 */
public class SnapshotSynchronizeServiceImpl implements SnapshotSynchronizeService {

    private SnapshotStore sourceSnapshotStore;
    private SnapshotStore targetSnapshotStore;
    private SnapshotService targetSnapshotService;
    private SnapshotSynchronizationAdjustment adjustor;

    /**
     * Constructor.
     * 
     * @param sourceSnapshotStore {@link SnapshotStore} to inject as synchronization source.
     * @param targetSnapshotStore {@link SnapshotStore} to inject as synchronization target.
     * @param targetSnapshotService {@link SnapshotService} to inject as synchronization target.
     * @param adjustor {@link SnapshotSynchronizationAdjustment} to inject.
     */
    public SnapshotSynchronizeServiceImpl(SnapshotStore sourceSnapshotStore,
            SnapshotStore targetSnapshotStore,
            SnapshotService targetSnapshotService,
            SnapshotSynchronizationAdjustment adjustor) {
        this.sourceSnapshotStore = sourceSnapshotStore;
        this.targetSnapshotStore = targetSnapshotStore;
        this.targetSnapshotService = targetSnapshotService;
        this.adjustor = adjustor;
    }

    @Override
    public final void synchronizeSnapshots() throws StatisticsException {
        // retrieve high-level snapshot information from the source
        List<Snapshot> sourceSnapshots = sourceSnapshotStore.getSnapshotsWithoutStatistics();
        // convert the snapshots to just a list of names
        List<String> sourceSnapshotNames = new ArrayList<String>(sourceSnapshots.size());
        for (Snapshot snapshot : sourceSnapshots) {
            sourceSnapshotNames.add(snapshot.getName());
        }
        // ask the target whether it contains the same snapshots
        Map<String, Snapshot> targetSnapshots =
                targetSnapshotStore.getSnapshotsWithoutStatistics(sourceSnapshotNames);

        // determine the snapshots to synchronize and do it
        for (String snapshotName : sourceSnapshotNames) {
            if (targetSnapshots.get(snapshotName) == null) {
                targetSnapshotService.writeSnapshot(adjustor
                        .adjustSnapshotForSynchronization(sourceSnapshotStore
                                .getSnapshot(snapshotName)));
            }
        }
    }

}
