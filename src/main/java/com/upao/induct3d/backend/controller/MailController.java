package com.upao.induct3d.backend.controller;

import com.upao.induct3d.backend.domain.EmailDTO;
import com.upao.induct3d.backend.service.IEmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sendMail")
public class MailController {

    private final IEmailService emailService;

    public MailController(IEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/simple")
    public ResponseEntity<?> receiveRequestEmail(@RequestBody EmailDTO emailDTO) {

        System.out.println("Message received " + emailDTO);

        emailService.sendEmail(emailDTO.getToUser(), emailDTO.getSubject(), emailDTO.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");

        return ResponseEntity.ok(response);
    }

}
