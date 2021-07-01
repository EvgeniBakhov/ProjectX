package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "picture")
public class PictureController {

    @Autowired
    PictureService pictureService;

    @GetMapping(produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getPicture(@RequestParam(value = "url", defaultValue = "") String url) {
        try {
            return this.pictureService.getPicture(url);
        } catch (IOException e) {
            return null;
        }
    }
}
