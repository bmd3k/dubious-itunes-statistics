package com.dubious.itunes.statistics.service;

import java.util.ArrayList;
import java.util.List;

import com.dubious.itunes.model.Song;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/**
 * Filters songs.
 */
public class SongFilter {

    /**
     * Filter a songs using rules for "various" artist albums.
     * 
     * @param songs The songs to filter.
     * @return The filtered songs.
     */
    public final List<Song> filterForVariousAlbum(List<Song> songs) {
        // iterate over the songs, counting the number of times each artist appears
        Multiset<String> artistCounts = HashMultiset.create();
        for (Song song : songs) {
            artistCounts.add(song.getArtistName());
        }

        // use the counts to determine which songs to copy to new list
        List<Song> filteredSongs = new ArrayList<Song>(songs.size());
        for (Song song : songs) {
            if (artistCounts.count(song.getArtistName()) < AlbumGrouping.GROUP_THRESHOLD) {
                filteredSongs.add(song);
            }
        }

        return filteredSongs;
    }
}
