package com.dubious.itunes.statistics.store.mongodb;

import com.mongodb.DB;

/**
 * Describes a mongodb-based data source.
 */
public interface MongoDbDataSource {

    /**
     * Retrieve the database used by this data source.
     * 
     * @return The database used by this data source.
     */
    DB getDB();

}
