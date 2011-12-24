package com.dubious.itunes.statistics.store.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.StoreException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * MongoDb storage of snapshots.
 */
public class MongoDbSnapshotStore implements SnapshotStore {

    private DB mongoDb;

    private static final String SNAPSHOT_COLLECTION_NAME = "snapshot";
    private static final String SNAPSHOT_NAME = "name";
    private static final String SNAPSHOT_DATE = "date";
    private static final String SNAPSHOT_STATISTICS = "statistics";

    private static final String SONG_ARTIST_NAME = "artist_name";
    private static final String SONG_ALBUM_NAME = "album_name";
    private static final String SONG_NAME = "name";

    private static final String STATISTICS_PLAY_COUNT = "play_count";

    public MongoDbSnapshotStore(DB mongoDb) {
        this.mongoDb = mongoDb;
    }

    @Override
    public Snapshot getSnapshot(String snapshotName) throws StoreException {
        DBCollection collection = mongoDb.getCollection(SNAPSHOT_COLLECTION_NAME);

        BasicDBObject query = new BasicDBObject();
        query.put(SNAPSHOT_NAME, snapshotName);
        BasicDBObject snapshotDoc = (BasicDBObject) collection.findOne(query);

        if (snapshotDoc == null) {
            return null;
        }

        Snapshot snapshot =
                new Snapshot()
                        .withName(snapshotDoc.getString(SNAPSHOT_NAME))
                        .withDate(getDateTime((Date) snapshotDoc.get(SNAPSHOT_DATE)));

        @SuppressWarnings("unchecked")
        List<BasicDBObject> statisticsDoc =
                (List<BasicDBObject>) snapshotDoc.get(SNAPSHOT_STATISTICS);
        for (BasicDBObject statisticDoc : statisticsDoc) {
            snapshot.addStatistic(new Song()
                    .withArtistName(statisticDoc.getString(SONG_ARTIST_NAME))
                    .withAlbumName(statisticDoc.getString(SONG_ALBUM_NAME))
                    .withName(statisticDoc.getString(SONG_NAME)),
                    new SongStatistics().withPlayCount(statisticDoc
                            .getInt(STATISTICS_PLAY_COUNT)));
        }

        return snapshot;
    }

    @Override
    public void writeSnapshot(Snapshot snapshot) {
        BasicDBObject snapshotDoc = new BasicDBObject();
        snapshotDoc.put(SNAPSHOT_NAME, snapshot.getName());
        snapshotDoc.put(SNAPSHOT_DATE, getDate(snapshot.getDate()));

        List<BasicDBObject> statisticsDoc =
                new ArrayList<BasicDBObject>(snapshot.getStatistics().size());
        for (Map.Entry<Song, SongStatistics> statistic : snapshot.getStatistics().entrySet()) {
            BasicDBObject statisticDoc = new BasicDBObject();
            statisticDoc.put(SONG_ARTIST_NAME, statistic.getKey().getArtistName());
            statisticDoc.put(SONG_ALBUM_NAME, statistic.getKey().getAlbumName());
            statisticDoc.put(SONG_NAME, statistic.getKey().getName());
            statisticDoc.put(STATISTICS_PLAY_COUNT, statistic.getValue().getPlayCount());
            statisticsDoc.add(statisticDoc);
        }
        snapshotDoc.put(SNAPSHOT_STATISTICS, statisticsDoc);

        DBCollection collection = mongoDb.getCollection(SNAPSHOT_COLLECTION_NAME);
        collection.insert(snapshotDoc);
    }

    @Override
    public void deleteAll() {
        DBCollection collection = mongoDb.getCollection(SNAPSHOT_COLLECTION_NAME);
        collection.remove(new BasicDBObject());
    }

    private Date getDate(DateTime jodaDateTime) {
        if (jodaDateTime == null) {
            return null;
        }
        return jodaDateTime.toDate();
    }

    private DateTime getDateTime(Date javaDate) {
        if (javaDate == null) {
            return null;
        }
        return new DateTime(((Date) javaDate).getTime());
    }
}
