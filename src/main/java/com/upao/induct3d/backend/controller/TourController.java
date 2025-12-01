package com.upao.induct3d.backend.controller;

import com.upao.induct3d.backend.domain.MessageDTO;
import com.upao.induct3d.backend.domain.request.CreateTourRequest;
import com.upao.induct3d.backend.domain.request.UpdateTourRequest;
import com.upao.induct3d.backend.domain.response.*;
import com.upao.induct3d.backend.entity.Tour;
import com.upao.induct3d.backend.entity.TourStatus;
import com.upao.induct3d.backend.exception.AttributeException;
import com.upao.induct3d.backend.exception.AuthUnauthorizedException;
import com.upao.induct3d.backend.exception.ResourceNotFoundException;
import com.upao.induct3d.backend.repository.TourRepository;
import com.upao.induct3d.backend.repository.UserRepository;
import com.upao.induct3d.backend.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AuthUnauthorizedException("No se ha enviado un token válido");
        }
        String username = auth.getName();
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new AuthUnauthorizedException("No se ha enviado un token válido"))
                .getId();
    }

    // Create tour
    @PostMapping("/create")
    @Operation(summary = "Create a tour", description = "Creates a new tour with steps and material colors.")
    public ResponseEntity<ApiResponse<CreateTourResponse>> createTour(@Valid @RequestBody CreateTourRequest request) {
        ObjectId userId = getCurrentUserId();
        Tour saved = tourService.createTour(request, userId);

        CreateTourResponse body = new CreateTourResponse(
                saved.getTourId(),
                saved.getTourName(),
                saved.getDescription(),
                saved.getStatus().toString()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(body));
    }

    // Get all tours
    @GetMapping
    @Operation(summary = "Get all tours", description = "Retrieves all tours available in the system.")
    public ResponseEntity<ApiResponse<List<TourListItemResponse>>> getAllTours(@RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false) Integer ignoredLimit) {
        final int limit = 9;
        Page<Tour> pageResult = tourService.getAllTours(page, limit);

        List<TourListItemResponse> items = pageResult.getContent().stream()
                .map(t -> new TourListItemResponse(
                        t.getTourId(),
                        t.getTourName(),
                        t.getDescription()
                ))
                .toList();

        long totalItems = pageResult.getTotalElements();
        int totalPages = pageResult.getTotalPages();

        Map<String, Object> meta = new HashMap<>();
        meta.put("page", page);
        meta.put("limit", limit);
        meta.put("totalItems", totalItems);
        meta.put("totalPages", totalPages);
        meta.put("hasNextPage", page < totalPages);
        meta.put("hasPrevPage", page > 1);

        ApiResponse<List<TourListItemResponse>> response = new ApiResponse<>(items, meta);
        return ResponseEntity.ok(response);
    }

    // Get all my tours
    @GetMapping("/my")
    @Operation(summary = "Get user's tours", description = "Retrieves all tours created by the authenticated user.")
    public ResponseEntity<ApiResponse<List<TourStatusItemResponse>>> getMyTours(@RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false) Integer ignoredLimit, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthUnauthorizedException("No se ha enviado un token válido");
        }

        ObjectId currentUserId = getCurrentUserId();
        final int limit = 9;
        Page<Tour> pageResult = tourService.getToursByUserPaginated(currentUserId, page, limit);
        List<TourStatusItemResponse> items = pageResult.getContent().stream()
                .map(TourStatusItemResponse::fromEntity)
                .toList();

        long totalItems = pageResult.getTotalElements();
        int totalPages = pageResult.getTotalPages();

        Map<String, Object> meta = new HashMap<>();
        meta.put("page", page);
        meta.put("limit", limit);
        meta.put("totalItems", totalItems);
        meta.put("totalPages", totalPages);
        meta.put("hasNextPage", page < totalPages);
        meta.put("hasPrevPage", page > 1);

        ApiResponse<List<TourStatusItemResponse>> response = new ApiResponse<>(items, meta);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<ApiResponse<UpdateTourResponse>> updateTour(@PathVariable String tourId, @Valid @RequestBody UpdateTourRequest request) {
        ObjectId currentUserId = getCurrentUserId();
        tourService.updateTour(tourId, request, currentUserId);
        UpdateTourResponse body = new UpdateTourResponse(tourId, "Tour actualizado correctamente");
        return ResponseEntity.ok(new ApiResponse<>(body));
    }

    // Delete tour
    @DeleteMapping("/{tourId}")
    @Operation(summary = "Delete a tour", description = "Deletes a tour by its ID.")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteTour(@PathVariable String tourId) {
        ObjectId currentUserId = getCurrentUserId();
        tourService.deleteTour(tourId, currentUserId);
        Map<String, String> body = Map.of("message", "Tour eliminado correctamente");
        return ResponseEntity.ok(new ApiResponse<>(body));
    }

    // Approve tour
    @PostMapping("/{tourId}/approve")
    @Operation(summary = "Approve tour", description = "Aprueba un tour y registra fecha en el historial.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TourResponse> approveTour(@PathVariable String tourId) throws ResourceNotFoundException {
        TourResponse resp = tourService.approveTour(tourId);
        return ResponseEntity.ok(resp);
    }

    // Reject tour
    @PostMapping("/{tourId}/reject")
    @Operation(summary = "Reject tour", description = "Rechaza un tour y registra motivo/fecha en el historial.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TourResponse> rejectTour(@PathVariable String tourId, @RequestBody Map<String, String> body) throws ResourceNotFoundException, AttributeException {
        String reason = body.getOrDefault("reason", "");
        TourResponse resp = tourService.rejectTour(tourId, reason);
        return ResponseEntity.ok(resp);
    }

    // List all tours for admin
    @GetMapping("/admin")
    @Operation(summary = "Admin - list all tours with status", description = "Lists all tours ordered by status: PENDING, REJECTED, APPROVED.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<TourStatusItemResponse>>> getAllToursForAdmin() {
        List<Tour> tours = tourService.getAllToursOrderedByStatus();
        List<TourStatusItemResponse> items = tours.stream().map(TourStatusItemResponse::fromEntity).toList();
        return ResponseEntity.ok(new ApiResponse<>(items));
    }

}
