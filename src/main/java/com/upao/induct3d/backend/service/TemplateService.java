package com.upao.induct3d.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.upao.induct3d.backend.entity.Template;
import org.springframework.beans.factory.annotation.Value;
import com.upao.induct3d.backend.exception.UploadException;
import com.upao.induct3d.backend.repository.TemplateRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class TemplateService {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Autowired private TemplateRepository templateRepo;
    @Autowired private Cloudinary cloudinary;

    public Template saveTemplate(String name, String description, List<MultipartFile> images, MultipartFile glbFile, ObjectId userId) {
        try {
            if (images.size() > 3) {
                throw new UploadException("Solo se permiten hasta 3 imágenes por template.");
            }

            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                validateImageFile(image);
                Map imageUpload = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                imageUrls.add((String) imageUpload.get("secure_url"));
            }

            validateGlbFile(glbFile);

            String uuid = UUID.randomUUID().toString();
            String publicId = "templates/modelo_" + uuid + ".glb";

            cloudinary.uploader().upload(
                    glbFile.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "raw",
                            "public_id", publicId,
                            "use_filename", false,
                            "unique_filename", false
                    )
            );

            String glbUrl = "https://res.cloudinary.com/" + cloudName + "/raw/upload/" + publicId;

            Template template = new Template();
            template.setName(name);
            template.setDescription(description);
            template.setImages(imageUrls);
            template.setGlbUrl(glbUrl);
            template.setUserId(userId);

            return templateRepo.save(template);

        } catch (IOException e) {
            throw new UploadException("Error al subir archivos a Cloudinary: " + e.getMessage());
        }
    }

    public List<Template> getTemplatesByUser(ObjectId userId) {
        return templateRepo.findByUserId(userId);
    }

    public Optional<String> getGlbUrlByTemplateId(String templateId) {
        return templateRepo.findById(templateId).map(Template::getGlbUrl);
    }

    private void validateGlbFile(MultipartFile file) {
        String filename = file.getOriginalFilename().toLowerCase();
        if (!filename.endsWith(".glb")) {
            throw new UploadException("Solo se permiten archivos .glb");
        }
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new UploadException("El archivo .glb no debe superar los 10MB");
        }
    }

    private void validateImageFile(MultipartFile file) {
        String filename = file.getOriginalFilename().toLowerCase();
        if (!(filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png"))) {
            throw new UploadException("Formato de imagen no válido. Usa .jpg, .jpeg o .png");
        }
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new UploadException("Cada imagen debe pesar menos de 2MB");
        }
    }

}


