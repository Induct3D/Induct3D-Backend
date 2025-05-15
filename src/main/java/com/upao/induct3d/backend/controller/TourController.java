package com.upao.induct3d.backend.controller;

import com.upao.induct3d.backend.domain.CreateTourRequest;
import com.upao.induct3d.backend.domain.UpdateTourRequest;
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
import java.util.stream.Collectors;

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

    @PostMapping("/create")
    public ResponseEntity<Tour> createTour(@RequestBody CreateTourRequest request) {
        ObjectId userId = getCurrentUserId();

        Tour tour = new Tour();
        tour.setTemplateId(new ObjectId(request.getTemplateId()));
        tour.setVoiceText(request.getVoiceText());
        tour.setMaterialColors(request.getMaterialColors());

        Tour saved = tourService.createTour(tour, userId);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Tour>> getMyTours() {
        ObjectId userId = getCurrentUserId();
        return ResponseEntity.ok(tourService.getToursByUser(userId));
    }

    @GetMapping("/{tourId}")
    public ResponseEntity<Tour> getTourById(@PathVariable String tourId) throws ResourceNotFoundException {
        return tourService.getTourById(tourId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Tour no encontrado"));
    }

    @PutMapping("/{tourId}")
    public ResponseEntity<Tour> updateTour(@PathVariable String tourId, @RequestBody UpdateTourRequest request) throws ResourceNotFoundException, AttributeException {
        ObjectId userId = getCurrentUserId();

        Tour existing = tourService.getTourById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour no encontrado"));

        if (!existing.getUserId().equals(userId)) {
            throw new AttributeException("No tienes permisos para editar este tour");
        }

        existing.setVoiceText(request.getVoiceText());
        existing.setMaterialColors(request.getMaterialColors());

        return ResponseEntity.ok(tourService.createTour(existing, userId));
    }

}


