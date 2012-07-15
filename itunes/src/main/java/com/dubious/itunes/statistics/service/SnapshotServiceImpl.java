package com.dubious.itunes.statistics.service;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.validate.SnapshotValidation;

/**
 * Implementation of {@link SnapshotService}.
 */
public class SnapshotServiceImpl implements SnapshotService {

    private SnapshotValidation snapshotValidation;
    private SnapshotStore snapshotStore;

    /**
     * Constructor.
     * 
     * @param snapshotValidation {@link SnapshotValidation} to inject.
     * @param snapshotStore {@link SnapshotStore} to inject.
     */
    public SnapshotServiceImpl(SnapshotValidation snapshotValidation, SnapshotStore snapshotStore) {
        this.snapshotValidation = snapshotValidation;
        this.snapshotStore = snapshotStore;
    }

    @Override
    public final void writeSnapshot(Snapshot snapshot) throws StatisticsException {
        snapshotValidation.validateOnWrite(snapshot);
        snapshotStore.writeSnapshot(snapshot);
    }

}
