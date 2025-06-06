package com.upao.induct3d.backend.controller;

import com.upao.induct3d.backend.domain.MessageDTO;
import com.upao.induct3d.backend.entity.Template;
import com.upao.induct3d.backend.exception.AttributeException;
import com.upao.induct3d.backend.exception.ResourceNotFoundException;
import com.upao.induct3d.backend.repository.TemplateRepository;
import com.upao.induct3d.backend.repository.UserRepository;
import com.upao.induct3d.backend.service.TemplateService;
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
    public ResponseEntity<List<Template>> getMyTemplates() {
        ObjectId userId = getCurrentUserId();
        List<Template> templates = templateService.getTemplatesByUser(userId);
        return ResponseEntity.ok(templates);
    }

    // Get all templates from everyone
    @GetMapping
    public ResponseEntity<List<Template>> getTemplates() {
        List<Template> templates = templateService.getTemplates();
        return ResponseEntity.ok(templates);
    }

    // Get url from GLB model
    @GetMapping("/glb/{templateId}")
    public ResponseEntity<String> getGlbUrl(@PathVariable String templateId) {
        return templateService.getGlbUrlByTemplateId(templateId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get template by ID
    @GetMapping("/{templateId}")
    public ResponseEntity<Template> getTemplateById(@PathVariable String templateId) throws ResourceNotFoundException {
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("Template no encontrado"));
        return ResponseEntity.ok(template);
    }
}
