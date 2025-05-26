package com.upao.induct3d.backend.service;

import com.upao.induct3d.backend.domain.request.UpdateTourRequest;
import com.upao.induct3d.backend.domain.response.TourResponse;
import com.upao.induct3d.backend.entity.Template;
import com.upao.induct3d.backend.entity.Tour;
import com.upao.induct3d.backend.exception.AttributeException;
import com.upao.induct3d.backend.exception.ResourceNotFoundException;
import com.upao.induct3d.backend.repository.TemplateRepository;
import com.upao.induct3d.backend.repository.TourRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourService {

    @Autowired private TourRepository tourRepository;

    @Autowired private TemplateRepository templateRepository;

    // Create tour
    public Tour createTour(Tour tour, ObjectId userId) {
        tour.setUserId(userId);
        return tourRepository.save(tour);
    }

    // Get tours for User
    public List<Tour> getToursByUser(ObjectId userId) {
        return tourRepository.findByUserId(userId);
    }

    // Get tour for TourID
    public Optional<TourResponse> getTourById(String id) {
        return tourRepository.findById(id)
                .map(t -> {
                    Template tpl = null;
                    try {
                        tpl = templateRepository
                                .findById(t.getTemplateId().toHexString())
                                .orElseThrow(() -> new ResourceNotFoundException("Template no existe"));
                    } catch (ResourceNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    return new TourResponse(
                            t.getTourId(),
                            t.getTourName(),
                            t.getDescription(),
                            t.getVoiceText(),
                            t.getMaterialColors(),
                            tpl.getGlbUrl()
                    );
                });
    }

    // Update tour for TourID
    public TourResponse updateTour(String tourId, UpdateTourRequest req, ObjectId currentUserId) throws ResourceNotFoundException, AttributeException {
        Tour existing = tourRepository.findById(tourId).orElseThrow(() -> new ResourceNotFoundException("Tour no encontrado"));

        if (!existing.getUserId().equals(currentUserId)) {
            throw new AttributeException("No tienes permisos para editar este tour");
        }

        existing.setTourName(req.getTourName());
        existing.setDescription(req.getDescription());
        existing.setVoiceText(req.getVoiceText());
        existing.setMaterialColors(req.getMaterialColors());
        existing.setSteps(req.getSteps());

        Tour saved = tourRepository.save(existing);
        Template tpl = templateRepository.findById(saved.getTemplateId().toHexString()).orElseThrow(() -> new ResourceNotFoundException("Template no existe"));
        return new TourResponse(saved.getTourId(), saved.getTourName(), saved.getDescription(), saved.getVoiceText(), saved.getMaterialColors(), tpl.getGlbUrl());
    }

    // Get tour for Template
    public List<Tour> getToursByTemplate(ObjectId templateId) {
        return tourRepository.findByTemplateId(templateId);
    }
}

