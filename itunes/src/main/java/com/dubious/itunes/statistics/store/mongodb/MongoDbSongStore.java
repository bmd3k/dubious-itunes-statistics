package com.dubious.itunes.statistics.store.mongodb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.dubious.itunes.model.Album;
import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.store.SongStore;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.WriteConcern;

/**
 * MongoDb storage of songs.
 */
public class MongoDbSongStore implements SongStore {

    /**
     * Name of Songs collection.
     */
    public static final String SONGS_COLLECTION_NAME = "song";
    /**
     * artist_name field in Songs collection.
     */
    public static final String SONGS_ARTIST_NAME = "artist_name";
    /**
     * album_name field in Songs collection.
     */
    public static final String SONGS_ALBUM_NAME = "album_name";
    /**
     * name field in Songs collection.
     */
    public static final String SONGS_SONG_NAME = "name";
    /**
     * track_number field in Songs collection.
     */
    public static final String SONGS_TRACK_NUMBER = "track_number";

    private DB mongoDb;
    private DBCollection songsCollection;

    /**
     * Constructor.
     * 
     * @param mongoDbDataSource The mongodb data source.
     */
    public MongoDbSongStore(MongoDbDataSource mongoDbDataSource) {
        this.mongoDb = mongoDbDataSource.getDB();
        songsCollection = mongoDb.getCollection(SONGS_COLLECTION_NAME);
        songsCollection.setWriteConcern(WriteConcern.SAFE);
    }

    @Override
    public final void writeSongs(Collection<Song> songs) {
        for (Song song : songs) {
            songsCollection.update(
                    new BasicDBObjectBuilder()
                            .add(SONGS_ARTIST_NAME, song.getArtistName())
                            .add(SONGS_ALBUM_NAME, song.getAlbumName())
                            .add(SONGS_SONG_NAME, song.getName())
                            .get(),
                    new BasicDBObjectBuilder()
                            .add(SONGS_ARTIST_NAME, song.getArtistName())
                            .add(SONGS_ALBUM_NAME, song.getAlbumName())
                            .add(SONGS_SONG_NAME, song.getName())
                            .add(SONGS_TRACK_NUMBER, song.getTrackNumber())
                            .get(),
                    true,
                    false);
        }
    }

    @Override
    public final List<Album> getAllAlbums() {
        // NOTE: group breaks in sharded environments
        // NOTE: group is inefficient with larger datasets
        // TODO: Convert group operations to map/reduce algorithm to solve both those issues

        // reduce command - count unique songs belonging to the album as we are grouping them
        // @formatter:off
        String reduce = 
                "function(obj,prev) { "
                    + " if(prev.songList[obj.name] == null)"
                        + "{"
                            + " prev.songList[obj.name] = 1;"
                            + " prev.count += 1; "
                        + "}"
                 + "}";
        // @formatter:on

        @SuppressWarnings("unchecked")
        List<BasicDBObject> albums =
                (List<BasicDBObject>) songsCollection.group(
                        new BasicDBObject().append(SONGS_ARTIST_NAME, true).append(
                                SONGS_ALBUM_NAME,
                                true),
                        new BasicDBObject(),
                        new BasicDBObject().append("count", 0).append(
                                "songList",
                                new BasicDBObject()),
                        reduce);

        List<Album> albumsToReturn = new ArrayList<Album>(albums.size());
        for (BasicDBObject album : albums) {
            albumsToReturn.add(new Album()
                    .withArtistName(album.getString(SONGS_ARTIST_NAME))
                    .withName(album.getString(SONGS_ALBUM_NAME))
                    .withSongCount(album.getInt("count")));
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
        DBCursor resultSet =
                songsCollection.find(
                        new BasicDBObject().append(SONGS_ARTIST_NAME, artistName).append(
                                SONGS_ALBUM_NAME,
                                albumName)).sort(
                        new BasicDBObject().append(SONGS_TRACK_NUMBER, 1));

        List<Song> songsToReturn = new ArrayList<Song>(resultSet.size());
        while (resultSet.hasNext()) {
            BasicDBObject song = (BasicDBObject) resultSet.next();
            songsToReturn.add(new Song()
                    .withArtistName(artistName)
                    .withAlbumName(albumName)
                    .withName(song.getString(SONGS_SONG_NAME))
                    .withTrackNumber(song.getInt(SONGS_TRACK_NUMBER)));
        }

        return songsToReturn;
    }
}
