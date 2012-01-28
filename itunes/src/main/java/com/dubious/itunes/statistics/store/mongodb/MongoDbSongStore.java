package com.dubious.itunes.statistics.store.mongodb;

import static com.dubious.itunes.statistics.store.mongodb.MongoDbSnapshotStore.SONG_STATISTICS_ALBUM_NAME;
import static com.dubious.itunes.statistics.store.mongodb.MongoDbSnapshotStore.SONG_STATISTICS_ARTIST_NAME;
import static com.dubious.itunes.statistics.store.mongodb.MongoDbSnapshotStore.SONG_STATISTICS_COLLECTION_NAME;
import static com.dubious.itunes.statistics.store.mongodb.MongoDbSnapshotStore.SONG_STATISTICS_SONG_NAME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.dubious.itunes.model.Album;
import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.store.SongStore;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.WriteConcern;

/**
 * MongoDb storage of songs.
 */
public class MongoDbSongStore implements SongStore {

    private DB mongoDb;
    private DBCollection songStatisticsCollection;

    /**
     * Constructor.
     * 
     * @param mongoDbDataSource The mongodb data source.
     */
    public MongoDbSongStore(MongoDbDataSource mongoDbDataSource) {
        this.mongoDb = mongoDbDataSource.getDB();
        songStatisticsCollection = mongoDb.getCollection(SONG_STATISTICS_COLLECTION_NAME);
        songStatisticsCollection.setWriteConcern(WriteConcern.SAFE);
    }

    @Override
    public final List<Album> getAllAlbums() {
        // NOTE: group breaks in sharded environments
        // NOTE: group is inefficient with larger datasets
        // TODO: Convert group operations to map/reduce algorithm to solve both those issues
        @SuppressWarnings("unchecked")
        List<BasicDBObject> albums =
                (List<BasicDBObject>) songStatisticsCollection.group(
                        new BasicDBObject().append(SONG_STATISTICS_ARTIST_NAME, true).append(
                                SONG_STATISTICS_ALBUM_NAME,
                                true),
                        new BasicDBObject(),
                        new BasicDBObject().append("count", 0),
                        "function(obj,prev) { prev.dummy += 1; }");

        List<Album> albumsToReturn = new ArrayList<Album>(albums.size());
        for (BasicDBObject album : albums) {
            albumsToReturn.add(new Album().withArtistName(
                    album.getString(SONG_STATISTICS_ARTIST_NAME)).withName(
                    album.getString(SONG_STATISTICS_ALBUM_NAME)));
        }

        Collections.sort(albumsToReturn, new Comparator<Album>() {

            @Override
            public int compare(Album album1, Album album2) {
                int artistCompare = album1.getArtistName().compareTo(album2.getArtistName());
                if (artistCompare != 0) {
                    return artistCompare;
                }

                return album1.getName().compareTo(album2.getName());
            }
        });

        return albumsToReturn;
    }

    @Override
    public final List<Song> getSongsForAlbum(String artistName, String albumName) {
        @SuppressWarnings("unchecked")
        List<BasicDBObject> songs =
                (List<BasicDBObject>) songStatisticsCollection.group(
                        new BasicDBObject().append(SONG_STATISTICS_SONG_NAME, true),
                        new BasicDBObject()
                                .append(SONG_STATISTICS_ARTIST_NAME, artistName)
                                .append(SONG_STATISTICS_ALBUM_NAME, albumName),
                        new BasicDBObject().append("count", 0),
                        "function(obj,prev) { prev.count += 1; }");

        List<Song> songsToReturn = new ArrayList<Song>(songs.size());
        for (BasicDBObject song : songs) {
            songsToReturn.add(new Song()
                    .withArtistName(artistName)
                    .withAlbumName(albumName)
                    .withName(song.getString(SONG_STATISTICS_SONG_NAME)));
        }

        Collections.sort(songsToReturn, new Comparator<Song>() {

            @Override
            public int compare(Song song1, Song song2) {
                return song1.getName().compareTo(song2.getName());
            }
        });

        return songsToReturn;
    }
}
