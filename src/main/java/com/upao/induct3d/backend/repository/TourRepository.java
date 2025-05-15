package com.upao.induct3d.backend.repository;

import com.upao.induct3d.backend.entity.Tour;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends MongoRepository<Tour, String> {
    List<Tour> findByUserId(ObjectId userId);
    List<Tour> findByTemplateId(ObjectId templateId);
}

