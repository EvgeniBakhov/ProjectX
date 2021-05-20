package com.projectx.ProjectX.service;

import com.projectx.ProjectX.model.Picture;
import com.projectx.ProjectX.repository.PictureRepository;
import com.projectx.ProjectX.util.FileSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class PictureService {

    @Autowired
    PictureRepository pictureRepository;

    public List<Picture> persistPictures(String uploadDir, MultipartFile[] pictures) throws IOException {
        FileSaver fileSaver = new FileSaver();
        List<Picture> pictureList = new ArrayList<>();
        int pos = 0;
        for(MultipartFile picture: pictures) {
            Path file = fileSaver.savePicture(picture, uploadDir);
            Picture newPicture = new Picture();
            newPicture.setPosition(pos++);
            newPicture.setUrl(file.toAbsolutePath().toString());
            newPicture = pictureRepository.save(newPicture);
            System.out.println("Picture saved with path:" + newPicture.getUrl());
            pictureList.add(newPicture);
        }
        return pictureList;
    }
}
