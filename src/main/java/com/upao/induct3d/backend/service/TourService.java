package com.upao.induct3d.backend.service;

import com.upao.induct3d.backend.domain.request.CreateTourRequest;
import com.upao.induct3d.backend.domain.request.UpdateTourRequest;
import com.upao.induct3d.backend.domain.response.TourResponse;
import com.upao.induct3d.backend.entity.Template;
import com.upao.induct3d.backend.entity.Tour;
import com.upao.induct3d.backend.entity.TourStatus;
import com.upao.induct3d.backend.exception.*;
import com.upao.induct3d.backend.repository.TemplateRepository;
import com.upao.induct3d.backend.repository.TourRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TourService {

    @Autowired private TourRepository tourRepository;
    @Autowired private TemplateRepository templateRepository;

    // Create tour
    public Tour createTour(CreateTourRequest req, ObjectId userId) {
        Template tpl = templateRepository.findById(req.getTemplateId())
                .orElseThrow(() -> new TemplateNotFoundException("El template seleccionado no existe"));
        Tour tour = new Tour();
        tour.setTemplateId(new ObjectId(tpl.getId()));
        tour.setUserId(userId);
        tour.setTourName(req.getTourName());
        tour.setDescription(req.getDescription());
        boolean hasPassword = Boolean.TRUE.equals(req.isHasPassword()) && req.getPassword() != null && !req.getPassword().isBlank();
        tour.setHasPassword(hasPassword);
        tour.setPassword(hasPassword ? req.getPassword() : null);
        tour.setMaterialColors(req.getMaterialColors());
        tour.setSteps(mapSteps(req.getSteps()));

        if (tour.getStatus() == null) {
            tour.setStatus(TourStatus.PENDING);
        }
        return tourRepository.save(tour);
    }

    private List<Tour.Step> mapSteps(List<CreateTourRequest.Step> dtoSteps) {
        if (dtoSteps == null) return List.of();

        return dtoSteps.stream().map(dto -> {
            Tour.Step step = new Tour.Step();
            step.setStepId(dto.getStepId());
            step.setMessages(dto.getMessages());

            if (dto.getBoardMedia() != null) {
                Tour.BoardMedia media = new Tour.BoardMedia();
                media.setHtml(dto.getBoardMedia().getHtml());
                step.setBoardMedia(media);
            }

            return step;
        }).toList();
    }

    // Get all tours
    public Page<Tour> getAllTours(int page, int limit) {
        int pageIndex = Math.max(page, 1) - 1;
        Pageable pageable = PageRequest.of(pageIndex, limit, Sort.by(Sort.Direction.DESC, "tourId"));
        Page<Tour> result = tourRepository.findByStatus(TourStatus.APPROVED, pageable);
        if (result.isEmpty()) {
            throw new ToursNotFoundException("No hay recorridos por listar");
        }
        return result;
    }

    // Get tours for User
    public List<Tour> getToursByUser(ObjectId userId) {
        return tourRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "tourId"));
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
    public TourResponse updateTour(String tourId, UpdateTourRequest req, ObjectId currentUserId) {
        Tour existing = tourRepository.findById(tourId).orElseThrow(() -> new TourNotFoundException("El tour solicitado no existe"));

        if (!existing.getUserId().equals(currentUserId)) {
            throw new ForbiddenTourAccessException("No tienes permisos para modificar este tour");
        }

        existing.setTourName(req.getTourName());
        existing.setDescription(req.getDescription());
        existing.setPassword(req.getPassword());
        existing.setHasPassword(req.isHasPassword());
        existing.setStatus(req.getStatus());
        existing.setMaterialColors(req.getMaterialColors());
        existing.setSteps(req.getSteps());

        Tour saved = tourRepository.save(existing);
        Template tpl = templateRepository.findById(saved.getTemplateId().toHexString()).orElseThrow(() -> new TemplateNotFoundException("El template seleccionado no existe"));
        return buildTourResponse(saved, tpl);
    }

    // Delete tour
    public void deleteTour(String tourId, ObjectId currentUserId) {
        Tour existing = tourRepository.findById(tourId).orElseThrow(() -> new TourNotFoundException("El tour solicitado no existe"));
        if (!existing.getUserId().equals(currentUserId)) {
            throw new ForbiddenTourAccessException("No tienes permisos para eliminar este tour");
        }
        tourRepository.delete(existing);
    }

    // Deactivate tours by User
    public void deactivateToursByUser(ObjectId userId) {
        List<Tour> userTours = tourRepository.findByUserId(userId);
        for (Tour tour : userTours) {
            tour.setStatus(TourStatus.INACTIVE);
            tourRepository.save(tour);
        }
    }

    // Get tour for Template
    public List<Tour> getToursByTemplate(ObjectId templateId) {
        return tourRepository.findByTemplateId(templateId);
    }

    // Get all tours ordered by status (admin)
    public List<Tour> getAllToursOrderedByStatus() {
        List<Tour> tours = tourRepository.findAll();
        Map<TourStatus, Integer> order = new HashMap<>();
        order.put(TourStatus.PENDING, 0);
        order.put(TourStatus.REJECTED, 1);
        order.put(TourStatus.APPROVED, 2);
        tours.sort(Comparator.comparingInt(t -> order.getOrDefault(t.getStatus(), 99)));
        return tours;
    }

    // TourResponse builder
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

        TourResponse resp = new TourResponse();
        resp.setTourId(tour.getTourId());
        resp.setTourName(tour.getTourName());
        resp.setDescription(tour.getDescription());
        resp.setHasPassword(tour.isHasPassword());
        resp.setStatus(tour.getStatus());
        resp.setReviewHistory(mapReviewNotes(tour.getReviewHistory()));
        resp.setMaterialColors(tour.getMaterialColors());
        resp.setGlbUrl(tpl.getGlbUrl());
        resp.setSteps(tour.getSteps());
        resp.setUserStart(userStart);
        resp.setPredefinedSteps(predefinedSteps);

        return resp;
    }

    public Page<Tour> getToursByUserPaginated(ObjectId userId, int page, int limit) {
        int pageIndex = Math.max(page, 1) - 1;
        Pageable pageable = PageRequest.of(pageIndex, limit, Sort.by(Sort.Direction.DESC, "tourId"));
        return tourRepository.findByUserId(userId, pageable);
    }

    private List<TourResponse.ReviewNote> mapReviewNotes(List<Tour.ReviewNote> src) {
        if (src == null) return List.of();
        return src.stream()
                .map(n -> new TourResponse.ReviewNote(n.getRejectionReason(), n.getReviewedAt()))
                .toList();
    }

    // Reject tour
    public TourResponse rejectTour(String tourId, String reason) throws ResourceNotFoundException, AttributeException {
        if (reason == null || reason.isBlank()) {
            throw new AttributeException("El motivo de rechazo es obligatorio");
        }

        Tour tour = tourRepository.findById(tourId).orElseThrow(() -> new ResourceNotFoundException("Tour no encontrado"));
        tour.setStatus(TourStatus.REJECTED);

        if (tour.getReviewHistory() == null) {
            tour.setReviewHistory(new ArrayList<>());
        }

        tour.getReviewHistory().add(new Tour.ReviewNote(reason, LocalDateTime.now()));

        Tour saved = tourRepository.save(tour);
        Template tpl = templateRepository.findById(saved.getTemplateId().toHexString()).orElseThrow(() -> new ResourceNotFoundException("Template no existe"));
        TourResponse resp = buildTourResponse(saved, tpl);

        resp.setHasPassword(saved.isHasPassword());
        resp.setStatus(saved.getStatus());
        return resp;
    }
}

