package com.projectx.ProjectX.service;

import com.projectx.ProjectX.util.FileSaver;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

@Service
public class PictureService {

    public Set<String> persistPictures(String uploadDir, MultipartFile[] pictures) throws IOException {
        FileSaver fileSaver = new FileSaver();
        Set<String> pictureList = new TreeSet<>();
        for(MultipartFile picture: pictures) {
            Path file = fileSaver.savePicture(picture, uploadDir);
            pictureList.add(file.toAbsolutePath().toString());
            System.out.println("Picture saved with path:" + file.toAbsolutePath().toString());
        }
        return pictureList;
    }

    public byte[] getPicture(String url) throws IOException {
        Path path = Paths.get(url);
        return Files.readAllBytes(path);
    }
}
