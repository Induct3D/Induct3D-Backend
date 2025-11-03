package com.upao.induct3d.backend.controller;

import com.upao.induct3d.backend.domain.MessageDTO;
import com.upao.induct3d.backend.domain.request.CreateTourRequest;
import com.upao.induct3d.backend.domain.request.UpdateTourRequest;
import com.upao.induct3d.backend.domain.response.TourResponse;
import com.upao.induct3d.backend.entity.Tour;
import com.upao.induct3d.backend.entity.TourStatus;
import com.upao.induct3d.backend.exception.AttributeException;
import com.upao.induct3d.backend.exception.ResourceNotFoundException;
import com.upao.induct3d.backend.repository.TourRepository;
import com.upao.induct3d.backend.repository.UserRepository;
import com.upao.induct3d.backend.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    @Autowired private TourService tourService;
    @Autowired private TourRepository tourRepository;
    @Autowired private UserRepository userRepository;

    private ObjectId getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();
    }

    // Create tour
    @PostMapping("/create")
    @Operation(summary = "Create a tour", description = "Creates a new tour with steps and material colors.")
    public ResponseEntity<Tour> createTour(@RequestBody CreateTourRequest request) {
        ObjectId userId = getCurrentUserId();

        Tour tour = new Tour();
        tour.setTemplateId(new ObjectId(request.getTemplateId()));
        tour.setTourName(request.getTourName());
        tour.setDescription(request.getDescription());
        tour.setPassword(request.getPassword());
        tour.setHasPassword(request.isHasPassword());
        tour.setStatus(request.getStatus() != null ? request.getStatus() : TourStatus.PENDING);
        tour.setMaterialColors(request.getMaterialColors());
        tour.setSteps(mapSteps(request.getSteps()));

        Tour saved = tourService.createTour(tour, userId);
        return ResponseEntity.ok(saved);
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
    @GetMapping
    @Operation(summary = "Get all tours", description = "Retrieves all tours available in the system.")
    public ResponseEntity<List<Tour>> getAllTours() {
        List<Tour> tours = tourService.getAllTours();
        return ResponseEntity.ok(tours);
    }

    // Get all my tours
    @GetMapping("/my")
    @Operation(summary = "Get user's tours", description = "Retrieves all tours created by the authenticated user.")
    public ResponseEntity<List<Tour>> getMyTours() {
        ObjectId userId = getCurrentUserId();
        return ResponseEntity.ok(tourService.getToursByUser(userId));
    }

    // Get a tour of ID
    @GetMapping("/{tourId}")
    @Operation(summary = "Get tour by ID", description = "Retrieves the details of a tour by its ID.")
    public ResponseEntity<TourResponse> getTourById(@PathVariable String tourId) throws ResourceNotFoundException {
        return tourService.getTourById(tourId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Tour no encontrado"));
    }

    // Update the tour of ID
    @PutMapping("/{tourId}")
    @Operation(summary = "Update a tour", description = "Updates the details of a tour by its ID.")
    public ResponseEntity<TourResponse> updateTour(@PathVariable String tourId, @RequestBody UpdateTourRequest request)
            throws ResourceNotFoundException, AttributeException {
        ObjectId userId = getCurrentUserId();
        TourResponse updated = tourService.updateTour(tourId, request, userId);
        return ResponseEntity.ok(updated);
    }

    // Delete tour
    @DeleteMapping("/{tourId}")
    @Operation(summary = "Delete a tour", description = "Deletes a tour by its ID.")
    public ResponseEntity<MessageDTO> deleteTour(@PathVariable String tourId) throws ResourceNotFoundException {
        Optional<Tour> optionalTour = tourRepository.findById(tourId);
        if (optionalTour.isEmpty()) {
            throw new ResourceNotFoundException("Tour no encontrado con ID: " + tourId);
        }

        tourRepository.deleteById(tourId);
        return ResponseEntity.ok(new MessageDTO(HttpStatus.OK, "Tour eliminado correctamente"));
    }

    // Reject tour
    @PostMapping("/{tourId}/reject")
    @Operation(summary = "Reject tour", description = "Rechaza un tour y registra motivo/fecha en el historial.")
    public ResponseEntity<TourResponse> rejectTour(
            @PathVariable String tourId,
            @RequestBody Map<String, String> body
    ) throws ResourceNotFoundException, AttributeException {
        String reason = body.getOrDefault("reason", "");
        TourResponse resp = tourService.rejectTour(tourId, reason);
        return ResponseEntity.ok(resp);
    }

}
