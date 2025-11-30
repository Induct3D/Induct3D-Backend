package com.upao.induct3d.backend.controller;

import com.upao.induct3d.backend.domain.MessageDTO;
import com.upao.induct3d.backend.domain.response.ApiResponse;
import com.upao.induct3d.backend.domain.response.TemplateResponse;
import com.upao.induct3d.backend.entity.Template;
import com.upao.induct3d.backend.exception.AttributeException;
import com.upao.induct3d.backend.exception.ResourceNotFoundException;
import com.upao.induct3d.backend.repository.TemplateRepository;
import com.upao.induct3d.backend.repository.UserRepository;
import com.upao.induct3d.backend.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    @Autowired private TemplateService templateService;
    @Autowired private UserRepository userRepository;
    @Autowired private TemplateRepository templateRepository;

    private ObjectId getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();
    }

    // Create template
    @PostMapping("/upload")
    @Operation(summary = "Upload a template", description = "Uploads a new template with images and GLB file.")
    public ResponseEntity<Template> uploadTemplate(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("glb") MultipartFile glbFile,
            @RequestParam(required = false) String userStart,
            @RequestParam(required = false) String predefinedSteps
    ) {
        ObjectId userId = null;
        Template saved = templateService.saveTemplate(name, description, images, glbFile, userStart, predefinedSteps, userId);
        return ResponseEntity.ok(saved);
    }

    // Get all template from creator
    @GetMapping("/my")
    @Operation(summary = "Get user's templates", description = "Retrieves all templates created by the authenticated user.")
    public ResponseEntity<List<Template>> getMyTemplates() {
        ObjectId userId = getCurrentUserId();
        List<Template> templates = templateService.getTemplatesByUser(userId);
        return ResponseEntity.ok(templates);
    }

    // Get all templates from everyone
    @GetMapping
    @Operation(summary = "Get all templates", description = "Retrieves all templates available in the system.")
    public ResponseEntity<ApiResponse<List<TemplateResponse>>> getTemplates() {
        List<TemplateResponse> templates = templateService.getTemplates();
        return ResponseEntity.ok(new ApiResponse<>(templates));
    }

    // Get url from GLB model
    @GetMapping("/glb/{templateId}")
    @Operation(summary = "Get GLB file URL", description = "Retrieves the URL of the GLB file for the specified template.")
    public ResponseEntity<String> getGlbUrl(@PathVariable String templateId) {
        return templateService.getGlbUrlByTemplateId(templateId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get template by ID
    @GetMapping("/{templateId}")
    @Operation(summary = "Get template by ID", description = "Retrieves the details of a template by its ID.")
    public ResponseEntity<ApiResponse<Template>> getTemplateById(@PathVariable String id) {
        Template template = templateService.getTemplateById(id);
        return ResponseEntity.ok(new ApiResponse<>(template));
    }
}
