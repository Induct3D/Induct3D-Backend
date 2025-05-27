package com.upao.induct3d.backend.controller;

import com.upao.induct3d.backend.domain.request.CreateTourRequest;
import com.upao.induct3d.backend.domain.request.UpdateTourRequest;
import com.upao.induct3d.backend.domain.response.TourResponse;
import com.upao.induct3d.backend.entity.Tour;
import com.upao.induct3d.backend.exception.AttributeException;
import com.upao.induct3d.backend.exception.ResourceNotFoundException;
import com.upao.induct3d.backend.repository.UserRepository;
import com.upao.induct3d.backend.service.TourService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    @Autowired private TourService tourService;
    @Autowired private UserRepository userRepository;

    private ObjectId getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();
    }

    // Create tour
    @PostMapping("/create")
    public ResponseEntity<Tour> createTour(@RequestBody CreateTourRequest request) {
        ObjectId userId = getCurrentUserId();

        Tour tour = new Tour();
        tour.setTemplateId(new ObjectId(request.getTemplateId()));
        tour.setTourName(request.getTourName());
        tour.setDescription(request.getDescription());
        tour.setMaterialColors(request.getMaterialColors());
        tour.setSteps(request.getSteps());

        Tour saved = tourService.createTour(tour, userId);
        return ResponseEntity.ok(saved);
    }

    // Get all my tours
    @GetMapping("/my")
    public ResponseEntity<List<Tour>> getMyTours() {
        ObjectId userId = getCurrentUserId();
        return ResponseEntity.ok(tourService.getToursByUser(userId));
    }

    // Get a tour of ID
    @GetMapping("/{tourId}")
    public ResponseEntity<TourResponse> getTourById(@PathVariable String tourId) throws ResourceNotFoundException {
        return tourService.getTourById(tourId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Tour no encontrado"));
    }

    // Update the tour of ID
    @PutMapping("/{tourId}")
    public ResponseEntity<TourResponse> updateTour(@PathVariable String tourId, @RequestBody UpdateTourRequest request)
            throws ResourceNotFoundException, AttributeException {
        ObjectId userId = getCurrentUserId();
        TourResponse updated = tourService.updateTour(tourId, request, userId);
        return ResponseEntity.ok(updated);
    }
}
