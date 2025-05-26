package com.upao.induct3d.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.upao.induct3d.backend.entity.Picture;
import com.upao.induct3d.backend.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class PictureService {

    @Autowired private Cloudinary cloudinary;
    @Autowired private PictureRepository pictureRepository;

    // Upload image and return URL
    public String upload(MultipartFile file) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Map<?,?> result = cloudinary.uploader()
                .upload(file.getBytes(),
                        ObjectUtils.asMap("folder", "tours"));

        String publicId = (String) result.get("public_id");
        String url      = (String) result.get("secure_url");

        Picture pic = new Picture(publicId, url, username);
        pictureRepository.save(pic);

        return url;
    }

    // Delete image in Cloudinary and MongoDB
    public void deleteByPublicId(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        pictureRepository.deleteById(publicId);
    }
}
