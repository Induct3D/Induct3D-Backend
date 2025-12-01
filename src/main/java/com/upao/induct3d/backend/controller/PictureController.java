package com.upao.induct3d.backend.controller;

import com.upao.induct3d.backend.domain.response.ApiResponse;
import com.upao.induct3d.backend.exception.FileRequiredException;
import com.upao.induct3d.backend.service.PictureService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Upload a picture", description = "Uploads a picture and returns its URL.")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadPicture(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileRequiredException("No se ha enviado ningún archivo");
        }
        String url = pictureService.upload(file);
        return ResponseEntity.ok(new ApiResponse<>(Map.of("url", url)));
    }

    // Delete image by ID
    @DeleteMapping("/{publicId}")
    @Operation(summary = "Delete a picture", description = "Deletes a picture by its public ID.")
    public ResponseEntity<?> delete(@PathVariable String publicId) throws IOException {
        pictureService.deleteByPublicId(publicId);
        return ResponseEntity.noContent().build();
    }
}

