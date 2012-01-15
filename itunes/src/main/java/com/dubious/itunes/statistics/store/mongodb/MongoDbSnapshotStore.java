package com.dubious.itunes.statistics.store.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.model.Snapshot;
import com.dubious.itunes.statistics.model.SongStatistics;
import com.dubious.itunes.statistics.store.SnapshotStore;
import com.dubious.itunes.statistics.store.StoreException;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBRef;
import com.mongodb.WriteConcern;

/**
 * MongoDb storage of snapshots.
 */
public class MongoDbSnapshotStore implements SnapshotStore {

    private DB mongoDb;
    private DBCollection snapshotCollection;
    private DBCollection songStatisticsCollection;

    private static final String SNAPSHOT_COLLECTION_NAME = "snapshot";
    private static final String SNAPSHOT_NAME = "_id";
    private static final String SNAPSHOT_DATE = "date";

    private static final String SONG_STATISTICS_COLLECTION_NAME = "song_history";
    private static final String SONG_STATISTICS_ARTIST_NAME = "artist_name";
    private static final String SONG_STATISTICS_ALBUM_NAME = "album_name";
    private static final String SONG_STATISTICS_SONG_NAME = "name";
    private static final String SONG_STATISTICS_SNAPSHOT_FK = "snapshot";

    private static final String SONG_STATISTICS_PLAY_COUNT = "play_count";

    /**
     * Constructor.
     * 
     * @param mongoDbDataSource The mongodb data source.
     */
    public MongoDbSnapshotStore(MongoDbDataSource mongoDbDataSource) {
        this.mongoDb = mongoDbDataSource.getDB();
        snapshotCollection = mongoDb.getCollection(SNAPSHOT_COLLECTION_NAME);
        snapshotCollection.setWriteConcern(WriteConcern.SAFE);
        songStatisticsCollection = mongoDb.getCollection(SONG_STATISTICS_COLLECTION_NAME);
        songStatisticsCollection.setWriteConcern(WriteConcern.SAFE);
    }

    @Override
    public final Snapshot getSnapshot(String snapshotName) throws StoreException {
        DBCursor resultSet =
                snapshotCollection.find(new BasicDBObjectBuilder().add(
                        SNAPSHOT_NAME,
                        snapshotName).get());

        BasicDBObject snapshotDoc =
                resultSet.hasNext() ? (BasicDBObject) resultSet.next() : null;
        if (snapshotDoc == null) {
            return null;
        }
        if (resultSet.hasNext()) {
            throw new MongoDbStoreException("Found multiple results for snapshot");
        }

        Snapshot snapshot = constructSnapshotFromDBObject(snapshotDoc);
        getSongStatistics(snapshot);
        return snapshot;
    }

    @Override
    public final List<Snapshot> getSnapshots() {
        DBCursor resultSet =
                snapshotCollection.find().sort(
                        new BasicDBObjectBuilder().add(SNAPSHOT_DATE, 1).get());

        List<Snapshot> snapshots = new ArrayList<Snapshot>(resultSet.size());
        while (resultSet.hasNext()) {
            Snapshot snapshot = constructSnapshotFromDBObject((BasicDBObject) resultSet.next());
            getSongStatistics(snapshot);
            snapshots.add(snapshot);
        }
        return snapshots;
    }

    /**
     * Construct base of a {@link Snapshot} instance from a {@link BasicDBObject} instance.
     * 
     * @param snapshotDoc The {@link BasicDBObject} instance to parse for information.
     * @return The snapshot.
     */
    private Snapshot constructSnapshotFromDBObject(BasicDBObject snapshotDoc) {
        return new Snapshot().withName(snapshotDoc.getString(SNAPSHOT_NAME)).withDate(
                getDateTime((Date) snapshotDoc.get(SNAPSHOT_DATE)));
    }

    /**
     * Retrieve all song statistics for a snapshot.
     * 
     * @param snapshot The snapshot for which to retrieve song statistics.
     */
    private void getSongStatistics(Snapshot snapshot) {
        BasicDBObject find = new BasicDBObject();
        find.put(SONG_STATISTICS_SNAPSHOT_FK, new DBRef(
                mongoDb,
                SNAPSHOT_COLLECTION_NAME,
                snapshot.getName()));
        BasicDBObject sort = new BasicDBObject();
        sort.put(SONG_STATISTICS_PLAY_COUNT, -1);
        DBCursor resultSet = songStatisticsCollection.find(find).sort(sort);

        while (resultSet.hasNext()) {
            BasicDBObject statisticsDoc = (BasicDBObject) resultSet.next();
            snapshot
                    .addStatistic(
                            new Song()
                                    .withArtistName(
                                            statisticsDoc.getString(SONG_STATISTICS_ARTIST_NAME))
                                    .withAlbumName(
                                            statisticsDoc.getString(SONG_STATISTICS_ALBUM_NAME))
                                    .withName(statisticsDoc.getString(SONG_STATISTICS_SONG_NAME)),
                            constructSongStatisticsFromDBObject(statisticsDoc));
        }
    }

    /**
     * Construct an {@link SongStatistics} instance from a {@link BasicDBObject} instance.
     * 
     * @param statisticsDoc The {@link BasicDBObject} instance to parse.
     * @return The song statistics.
     */
    private SongStatistics constructSongStatisticsFromDBObject(BasicDBObject statisticsDoc) {
        return new SongStatistics().withPlayCount(statisticsDoc
                .getInt(SONG_STATISTICS_PLAY_COUNT));
    }

    @Override
    public final void writeSnapshot(Snapshot snapshot) {
        snapshotCollection.insert(new BasicDBObjectBuilder()
                .add(SNAPSHOT_NAME, snapshot.getName())
                .add(SNAPSHOT_DATE, getDate(snapshot.getDate()))
                .get());

        writeSongStatistics(snapshot);
    }

    /**
     * Write song statistics from a snapshot to the data store.
     * 
     * @param snapshot The snapshot with the song statistics information.
     */
    private void writeSongStatistics(Snapshot snapshot) {
        for (Map.Entry<Song, SongStatistics> statistics : snapshot.getStatistics().entrySet()) {
            songStatisticsCollection.insert(new BasicDBObjectBuilder()
                    .add(
                            SONG_STATISTICS_SNAPSHOT_FK,
                            new DBRef(mongoDb, SNAPSHOT_COLLECTION_NAME, snapshot.getName()))
                    .add(SONG_STATISTICS_ARTIST_NAME, statistics.getKey().getArtistName())
                    .add(SONG_STATISTICS_ALBUM_NAME, statistics.getKey().getAlbumName())
                    .add(SONG_STATISTICS_SONG_NAME, statistics.getKey().getName())
                    .add(SONG_STATISTICS_PLAY_COUNT, statistics.getValue().getPlayCount())
                    .get());
        }
    }

    @Override
    public final void deleteAll() {
        mongoDb.getCollection(SNAPSHOT_COLLECTION_NAME).remove(new BasicDBObject());
        mongoDb.getCollection(SONG_STATISTICS_COLLECTION_NAME).remove(new BasicDBObject());
    }

    /**
     * Get the {@link Date} corresponding to a joda {@link DateTime}.
     * 
     * @param jodaDateTime The joda date time.
     * @return The date.
     */
    private Date getDate(DateTime jodaDateTime) {
        if (jodaDateTime == null) {
            return null;
        }
        return jodaDateTime.toDate();
    }

    /**
     * Get the joda {@link DateTime} corresponding to a {@link Date}.
     * 
     * @param javaDate The java date.
     * @return The joda date time.
     */
    private DateTime getDateTime(Date javaDate) {
        if (javaDate == null) {
            return null;
        }
        return new DateTime(((Date) javaDate).getTime());
    }

    @Override
    public final Map<String, Snapshot> getSnapshotsWithoutStatistics(List<String> snapshotNames) {
        // first, generate a list of snapshot objects
        BasicDBObject query = new BasicDBObject();
        query.put(SNAPSHOT_NAME, new BasicDBObject("$in", snapshotNames));
        DBCursor resultSet = snapshotCollection.find(query);

        Map<String, Snapshot> snapshots = new HashMap<String, Snapshot>();
        while (resultSet.hasNext()) {
            Snapshot snapshot = constructSnapshotFromDBObject((BasicDBObject) resultSet.next());
            snapshots.put(snapshot.getName(), snapshot);
        }

        return snapshots;
    }

    @Override
    public final List<Snapshot> getSnapshotsWithoutStatistics() {
        DBCursor resultSet =
                snapshotCollection.find().sort(
                        new BasicDBObjectBuilder().add(SNAPSHOT_DATE, 1).get());

        List<Snapshot> snapshots = new ArrayList<Snapshot>(resultSet.size());
        while (resultSet.hasNext()) {
            snapshots.add(constructSnapshotFromDBObject((BasicDBObject) resultSet.next()));
        }
        return snapshots;
    }

    @Override
    public final Map<String, SongStatistics> getSongStatisticsFromSnapshots(
            String artistName,
            String albumName,
            String songName,
            List<String> snapshotNames) {

        // lookup is done using DBRef objects
        List<DBRef> snapshotRefs = new ArrayList<DBRef>(snapshotNames.size());
        for (String snapshotName : snapshotNames) {
            snapshotRefs.add(new DBRef(mongoDb, SNAPSHOT_COLLECTION_NAME, snapshotName));
        }
        //@formatter:off
        DBCursor resultSet =
                songStatisticsCollection.find(new BasicDBObjectBuilder()
                                .add(SONG_STATISTICS_SNAPSHOT_FK,
                                        new BasicDBObject("$in", snapshotRefs))
                                .add(SONG_STATISTICS_ARTIST_NAME, artistName)
                                .add(SONG_STATISTICS_ALBUM_NAME, albumName)
                                .add(SONG_STATISTICS_SONG_NAME, songName)
                                .get());
        //@formatter:on

        Map<String, SongStatistics> allStatistics = new HashMap<String, SongStatistics>();
        while (resultSet.hasNext()) {
            BasicDBObject statisticsDoc = (BasicDBObject) resultSet.next();
            allStatistics.put(((DBRef) statisticsDoc.get(SONG_STATISTICS_SNAPSHOT_FK))
                    .getId()
                    .toString(), constructSongStatisticsFromDBObject(statisticsDoc));
        }

        return allStatistics;
    }
}
