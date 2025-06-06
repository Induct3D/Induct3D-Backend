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
import java.util.stream.Collectors;

@Service
public class TourService {

    @Autowired private TourRepository tourRepository;

    @Autowired private TemplateRepository templateRepository;

    // Create tour
    public Tour createTour(Tour tour, ObjectId userId) {
        tour.setUserId(userId);
        return tourRepository.save(tour);
    }

    // Get all tours
    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    // Get tours for User
    public List<Tour> getToursByUser(ObjectId userId) {
        return tourRepository.findByUserId(userId);
    }

    // Get tour for TourID
    public Optional<TourResponse> getTourById(String id) {
        return tourRepository.findById(id).map(tour -> {
                    Template tpl = null;
                    try {
                        tpl = templateRepository.findById(tour.getTemplateId().toHexString())
                                .orElseThrow(() -> new ResourceNotFoundException("Template no existe"));
                    } catch (ResourceNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    return buildTourResponse(tour, tpl);
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
        existing.setMaterialColors(req.getMaterialColors());
        existing.setSteps(req.getSteps());

        Tour saved = tourRepository.save(existing);
        Template tpl = templateRepository.findById(saved.getTemplateId().toHexString()).orElseThrow(() -> new ResourceNotFoundException("Template no existe"));
        return buildTourResponse(saved, tpl);
    }

    // Get tour for Template
    public List<Tour> getToursByTemplate(ObjectId templateId) {
        return tourRepository.findByTemplateId(templateId);
    }

    private TourResponse buildTourResponse(Tour tour, Template tpl) {
        TourResponse.Vector3 userStart = new TourResponse.Vector3(
                tpl.getUserStart().getX(),
                tpl.getUserStart().getY(),
                tpl.getUserStart().getZ()
        );

        List<TourResponse.PredefinedStep> predefinedSteps = tpl.getPredefinedSteps()
                .stream()
                .map(ps -> {
                    List<TourResponse.Vector3> stepPositions = ps.getPosition().stream()
                            .map(v -> new TourResponse.Vector3(v.getX(), v.getY(), v.getZ()))
                            .collect(Collectors.toList());

                    // boardConfig puede ser null
                    TourResponse.BoardConfig boardConfig = null;
                    if (ps.getBoardConfig() != null) {
                        Template.BoardConfig bc = ps.getBoardConfig();
                        boardConfig = new TourResponse.BoardConfig(
                                new TourResponse.Vector3(bc.getPosition().getX(), bc.getPosition().getY(), bc.getPosition().getZ()),
                                new TourResponse.Vector3(bc.getRotation().getX(), bc.getRotation().getY(), bc.getRotation().getZ()),
                                bc.getScale()
                        );
                    }

                    return new TourResponse.PredefinedStep(
                            ps.getId(),
                            stepPositions,
                            ps.getHasBoard(),
                            boardConfig
                    );
                })
                .collect(Collectors.toList());

        return new TourResponse(
                tour.getTourId(),
                tour.getTourName(),
                tour.getDescription(),
                tour.getMaterialColors(),
                tpl.getGlbUrl(),
                tour.getSteps(),
                userStart,
                predefinedSteps
        );
    }

}

