package com.upao.induct3d.backend.repository;

import com.upao.induct3d.backend.entity.Template;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends MongoRepository<Template, String> {
    List<Template> findByUserId(ObjectId userId);
}


