package com.dubious.itunes.web;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dubious.itunes.model.Album;
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
        List<SongTreeAlbum> songTreeAlbums = new ArrayList<SongTreeAlbum>();
        for (Album album : albums) {
            songTreeAlbums.add(new SongTreeAlbum()
                    .withArtistName(album.getArtistName())
                    .withAlbumName(album.getName()));
        }
        return songTreeAlbums;
    }

    /**
     * Get some leaf nodes for a specific top-level item.
     * 
     * @param artistName The artist name.
     * @param albumName The album name.
     * @return The leaf nodes.
     */
    @RequestMapping(value = "/song/getSongTreeSongs.do", method = RequestMethod.GET)
    @ResponseBody
    public final List<SongTreeSong> getSongTreeSongs(String artistName, String albumName) {
        if (artistName.equals("Radiohead") && albumName.equals("Kid A")) {
            return asList(new SongTreeSong()
                    .withArtistName("Radiohead")
                    .withAlbumName("Kid A")
                    .withSongName("Morning Bell"), new SongTreeSong()
                    .withArtistName("Radiohead")
                    .withAlbumName("Kid A")
                    .withSongName("Optimistic"));
        } else if (artistName.equals("Animal Collective") && albumName.equals("Strawberry Jam")) {
            return asList(
                    new SongTreeSong()
                            .withArtistName("Animal Collective")
                            .withAlbumName("Strawberry Jam")
                            .withSongName("Peacebone"),
                    new SongTreeSong()
                            .withArtistName("Animal Collective")
                            .withAlbumName("Strawberry Jam")
                            .withSongName("Derek"));

        }

        return Collections.<SongTreeSong>emptyList();
    }
}
