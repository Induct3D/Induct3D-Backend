package com.upao.induct3d.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.upao.induct3d.backend.entity.Picture;
import com.upao.induct3d.backend.exception.AuthUnauthorizedException;
import com.upao.induct3d.backend.exception.UploadException;
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
    public String upload(MultipartFile file) {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                throw new AuthUnauthorizedException("No se ha enviado un token válido");
            }

            String username = auth.getName();
            Map<?,?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "tours",
                            "quality", 100,
                            "bytes", 10485760 // máximo 10 MB
                    )
            );

            if (result == null || result.get("secure_url") == null) {
                throw new UploadException("No se pudo subir la imagen");
            }

            String publicId = (String) result.get("public_id");
            String url = (String) result.get("secure_url");
            Picture pic = new Picture(publicId, url, username);
            pictureRepository.save(pic);
            return url;

        } catch (AuthUnauthorizedException e) {
            throw e;

        } catch (Exception e) {
            throw new UploadException("No se pudo subir la imagen");
        }
    }

    // Delete image in Cloudinary and MongoDB
    public void deleteByPublicId(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        pictureRepository.deleteById(publicId);
    }
}
