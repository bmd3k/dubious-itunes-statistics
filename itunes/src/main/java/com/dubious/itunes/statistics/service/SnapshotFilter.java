package com.dubious.itunes.statistics.service;

import static java.util.Collections.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.dubious.itunes.statistics.exception.SnapshotForQuarterNotFoundException;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.model.Snapshot;

/**
 * Methods for filtering and ordering snapshots into sublists.
 */
public class SnapshotFilter {

    /**
     * Filter by choosing only one snapshot for each quarter. The snapshot chosen is the latest for
     * the quarter. The returned snapshots are ordered by quarter.
     * 
     * @param snapshots The snapshots to filter and order.
     * @return The filtered and ordered snapshots.
     * @throws StatisticsException On error.
     */
    public final List<Snapshot> filterByLastInQuarter(List<Snapshot> snapshots)
            throws StatisticsException {
        if (snapshots.size() == 0) {
            return Collections.<Snapshot>emptyList();
        }

        List<Snapshot> sortedSnapshots = new ArrayList<Snapshot>(snapshots);
        sortSnapshotsByDate(sortedSnapshots);

        // List is now sorted. Iterate through the snapshots and find the latest in each quarter.
        List<Snapshot> filteredSnapshots = new ArrayList<Snapshot>();
        YearAndQuarter currentQuarter =
                YearAndQuarter.fromDateTime(sortedSnapshots.get(0).getDate());
        filteredSnapshots.add(sortedSnapshots.get(0));
        for (Snapshot snapshot : sortedSnapshots) {
            YearAndQuarter nextQuarter = YearAndQuarter.fromDateTime(snapshot.getDate());
            if (nextQuarter.equals(currentQuarter)) {
                // this is a later snapshot in the same quarter. replace the last element.
                filteredSnapshots.set(filteredSnapshots.size() - 1, snapshot);
            } else {
                // this is a new quarter
                // ensure this new quarter is the one following directly behind the last
                if (currentQuarter.compareTo(nextQuarter) != -1) {
                    throw new SnapshotForQuarterNotFoundException(currentQuarter.addQuarters(1));
                }
                currentQuarter = nextQuarter;
                filteredSnapshots.add(snapshot);
            }
        }

        return filteredSnapshots;
    }

    /**
     * Sort snapshots by the date of the snapshots.
     * 
     * @param snapshots The snapshots to sort.
     */
    public final void sortSnapshotsByDate(List<Snapshot> snapshots) {
        sort(snapshots, new Comparator<Snapshot>() {
            @Override
            public int compare(Snapshot first, Snapshot second) {
                return first.getDate().compareTo(second.getDate());
            }
        });
    }
}
