package com.shanzhu.music.controller;


import com.shanzhu.music.entity.po.Song;
import com.shanzhu.music.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MusicController {
    @Autowired
    private SongService songService;

    @RequestMapping("/music")
    public List<Song> findAll(){
    return songService.allSong();}
}
