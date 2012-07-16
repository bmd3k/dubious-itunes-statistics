package com.dubious.itunes.statistics.service;

import java.util.ArrayList;
import java.util.List;

import com.dubious.itunes.model.Album;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * Implementation of {@liink AlbumGrouping}.
 */
public class AlbumGrouper implements AlbumGrouping {

    @Override
    public final List<Album> groupAlbums(List<Album> albums) {
        List<Album> groupedAlbums = new ArrayList<Album>(albums.size());

        // iterate over the albums, looking for grouping candidates
        ListMultimap<String, Album> groupingCandidates =
                LinkedListMultimap.create(albums.size());
        for (Album album : albums) {
            if (album.getSongCount() < GROUP_THRESHOLD) {
                groupingCandidates.put(album.getName(), album);
            }
        }

        // iterate again, looking for non-groupings and eliminating potential groupings where not
        // appropriate
        for (Album album : albums) {
            List<Album> albumsOfSameName = groupingCandidates.get(album.getName());
            if (albumsOfSameName == null || album.getSongCount() >= GROUP_THRESHOLD) {
                // no candidate grouping for this album name
                // or there is a candidate grouping but this album is not part of the group
                groupedAlbums.add(album);
            } else if (albumsOfSameName.size() == 1) {
                // there is a candidate group but since it only has album there is nothing to group
                groupedAlbums.add(album);
                groupingCandidates.removeAll(album.getName());
            }
        }

        // iterate over remaining candidate groupings and group them
        for (String albumName : groupingCandidates.keySet()) {
            int songCount = 0;
            for (Album album : groupingCandidates.get(albumName)) {
                songCount += album.getSongCount();
            }
            groupedAlbums.add(new Album()
                    .withArtistName(GROUPED_ALBUM_ARTIST_NAME)
                    .withName(albumName)
                    .withSongCount(songCount));
        }

        return groupedAlbums;
    }

}
