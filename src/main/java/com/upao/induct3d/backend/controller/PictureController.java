package com.upao.induct3d.backend.controller;

import com.upao.induct3d.backend.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/pictures")
public class PictureController {

    @Autowired private PictureService pictureService;

    // Upload images
    @PostMapping("/upload")
    public ResponseEntity<Map<String,String>> upload(
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        String url = pictureService.upload(file);
        return ResponseEntity.ok(Collections.singletonMap("url", url));
    }

    // Delete image by ID
    @DeleteMapping("/{publicId}")
    public ResponseEntity<?> delete(@PathVariable String publicId) throws IOException {
        pictureService.deleteByPublicId(publicId);
        return ResponseEntity.noContent().build();
    }
}

