package com.upao.induct3d.backend.repository;

import com.upao.induct3d.backend.entity.Tour;
import com.upao.induct3d.backend.entity.TourStatus;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends MongoRepository<Tour, String> {
    List<Tour> findByUserId(ObjectId userId);
    List<Tour> findByUserId(ObjectId userId, Sort sort);
    Page<Tour> findByUserId(ObjectId userId, Pageable pageable);
    List<Tour> findByTemplateId(ObjectId templateId);
    List<Tour> findByStatus(TourStatus status);
}

