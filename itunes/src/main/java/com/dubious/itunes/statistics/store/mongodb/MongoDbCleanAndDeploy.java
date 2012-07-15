package com.dubious.itunes.statistics.store.mongodb;

import com.dubious.itunes.statistics.store.CleanAndDeploy;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.WriteConcern;

/**
 * Clean And Deploy MongoDB-based statistics store.
 */
public class MongoDbCleanAndDeploy implements CleanAndDeploy {

    private DB mongoDb;
    private DBCollection snapshotCollection;
    private DBCollection songsCollection;
    private DBCollection songStatisticsCollection;

    /**
     * Constructor.
     * 
     * @param mongoDbDataSource {@link MongoDbDataSource} to inject.
     */
    public MongoDbCleanAndDeploy(MongoDbDataSource mongoDbDataSource) {
        this.mongoDb = mongoDbDataSource.getDB();
        snapshotCollection =
                mongoDb.getCollection(MongoDbSnapshotStore.SNAPSHOT_COLLECTION_NAME);
        snapshotCollection.setWriteConcern(WriteConcern.SAFE);
        songStatisticsCollection =
                mongoDb.getCollection(MongoDbSnapshotStore.SONG_STATISTICS_COLLECTION_NAME);
        songStatisticsCollection.setWriteConcern(WriteConcern.SAFE);
        songsCollection = mongoDb.getCollection(MongoDbSongStore.SONGS_COLLECTION_NAME);
        songsCollection.setWriteConcern(WriteConcern.SAFE);
    }

    @Override
    public final void cleanAndDeploy() {
        // Drop Existing Tables
        snapshotCollection.drop();
        songsCollection.drop();
        songStatisticsCollection.drop();

        // Create the new tables by specifying their indeces
        snapshotCollection.createIndex(new BasicDBObject().append(
                MongoDbSnapshotStore.SNAPSHOT_NAME,
                1));
        songsCollection.createIndex(new BasicDBObject()
                .append(MongoDbSongStore.SONGS_ARTIST_NAME, 1)
                .append(MongoDbSongStore.SONGS_ALBUM_NAME, 1)
                .append(MongoDbSongStore.SONGS_SONG_NAME, 1));
        songStatisticsCollection.createIndex(new BasicDBObject()
                .append(MongoDbSongStore.SONGS_ARTIST_NAME, 1)
                .append(MongoDbSongStore.SONGS_ALBUM_NAME, 1)
                .append(MongoDbSongStore.SONGS_SONG_NAME, 1));
    }
}
