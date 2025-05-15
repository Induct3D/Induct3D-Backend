package com.upao.induct3d.backend.service;

import com.upao.induct3d.backend.entity.Tour;
import com.upao.induct3d.backend.repository.TourRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourService {

    @Autowired private TourRepository tourRepository;

    public Tour createTour(Tour tour, ObjectId userId) {
        tour.setUserId(userId);
        return tourRepository.save(tour);
    }

    public List<Tour> getToursByUser(ObjectId userId) {
        return tourRepository.findByUserId(userId);
    }

    public Optional<Tour> getTourById(String tourId) {
        return tourRepository.findById(tourId);
    }

    public List<Tour> getToursByTemplate(ObjectId templateId) {
        return tourRepository.findByTemplateId(templateId);
    }
}

