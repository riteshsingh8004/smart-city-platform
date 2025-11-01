package com.smartcity.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;

@Service
public class ImageService {

    private final Path root = Paths.get("uploads");

    public String uploadImage(MultipartFile file) {
        try {
            if (!Files.exists(root)) Files.createDirectories(root);
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), root.resolve(fileName));
            return "/uploads/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Could not upload file: " + e.getMessage());
        }
    }
}
