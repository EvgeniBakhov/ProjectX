package com.projectx.ProjectX.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class FileSaver {

    public Path savePicture(MultipartFile picture, String uploadDir) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Date date = new Date();
        Path filePath = uploadPath.resolve(date.getTime() + picture.getOriginalFilename());
        Path file = Files.createFile(filePath);
        picture.transferTo(file);
        return file;
    }
}
