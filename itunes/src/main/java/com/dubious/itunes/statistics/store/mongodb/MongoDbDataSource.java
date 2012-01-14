package com.dubious.itunes.statistics.store.mongodb;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.Mongo;

/**
 * Maintains a MongoDb data source.
 */
public class MongoDbDataSource {

    private DB mongoDb;

    /**
     * Construct and initialize the data source.
     * 
     * @param host The host location of the server.
     * @param dbName The name of the db to use.
     * @throws MongoDbStoreException On error.
     */
    public MongoDbDataSource(String host, String dbName) throws MongoDbStoreException {
        try {
            mongoDb = new Mongo(host).getDB(dbName);
        } catch (UnknownHostException e) {
            throw new MongoDbStoreException("Could not connect to host", e);
        }
    }

    /**
     * Retrieve the database used by this data source.
     * 
     * @return The database used by this data source.
     */
    public final DB getDB() {
        return mongoDb;
    }
}
