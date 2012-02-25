package com.dubious.itunes.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dubious.itunes.model.Album;
import com.dubious.itunes.model.Song;
import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.service.SongService;

/**
 * Controller for the song tree component.
 */
@Controller
public class SongTreeController {

    @Resource(name = "songService")
    private SongService songService;

    /**
     * Get the top level items in the tree.
     * 
     * @return The top level items in the tree.
     */
    @RequestMapping(value = "/song/getSongTreeAlbums.do", method = RequestMethod.GET)
    @ResponseBody
    public final List<SongTreeAlbum> getSongTreeAlbums() {
        List<Album> albums = songService.getAllAlbums();
        List<SongTreeAlbum> songTreeAlbums = new ArrayList<SongTreeAlbum>(albums.size());
        for (Album album : albums) {
            if (album.getSongCount() > 4) {
                songTreeAlbums.add(new SongTreeAlbum()
                        .withArtistName(album.getArtistName())
                        .withAlbumName(album.getName()));
            }
        }
        return songTreeAlbums;
    }

    /**
     * Get some leaf nodes for a specific top-level item.
     * 
     * @param artistName The artist name.
     * @param albumName The album name.
     * @return The leaf nodes.
     * @throws StatisticsException On error.
     */
    @RequestMapping(value = "/song/getSongTreeSongs.do", method = RequestMethod.GET)
    @ResponseBody
    public final List<SongTreeSong> getSongTreeSongs(String artistName, String albumName)
            throws StatisticsException {

        List<Song> songs = songService.getSongsForAlbum(artistName, albumName);
        List<SongTreeSong> songTreeSongs = new ArrayList<SongTreeSong>(songs.size());
        for (Song song : songs) {
            songTreeSongs.add(new SongTreeSong()
                    .withArtistName(song.getArtistName())
                    .withAlbumName(song.getAlbumName())
                    .withSongName(song.getName()));
        }
        return songTreeSongs;
    }
}
