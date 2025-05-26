package com.upao.induct3d.backend.repository;

import com.upao.induct3d.backend.entity.Picture;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends MongoRepository<Picture,String> {
    List<Picture> findByUploadedBy(String uploadedBy);
}
