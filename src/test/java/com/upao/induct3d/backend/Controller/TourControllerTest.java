package com.upao.induct3d.backend.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upao.induct3d.backend.controller.TourController;
import com.upao.induct3d.backend.domain.request.CreateTourRequest;
import com.upao.induct3d.backend.entity.Tour;
import com.upao.induct3d.backend.entity.User;
import com.upao.induct3d.backend.repository.TourRepository;
import com.upao.induct3d.backend.repository.UserRepository;
import com.upao.induct3d.backend.service.TourService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@WebMvcTest(
        controllers = TourController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.upao.induct3d.backend.jwt.JwtFilter.class)
        }
)
@WithMockUser(username = "creator@example.com")
public class TourControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private TourService tourService;
    @MockBean private TourRepository tourRepository;
    @MockBean private UserRepository userRepository;

    @Test
    @DisplayName("T5: Debería crear un tour válido con steps y materiales")
    void shouldCreateTourSuccessfully() throws Exception {
        ObjectId userId = new ObjectId("60c72b2f9b1e8a2f9c8e4f12");

        // Paso del request
        CreateTourRequest.Step step = new CreateTourRequest.Step();
        step.setStepId("paso-1");
        step.setMessages(List.of("Bienvenidos al recorrido"));
        CreateTourRequest.BoardMedia boardMedia = new CreateTourRequest.BoardMedia();
        boardMedia.setHtml("<p>Contenido interactivo</p>");
        step.setBoardMedia(boardMedia);

        CreateTourRequest request = new CreateTourRequest();
        request.setTemplateId(new ObjectId().toHexString());
        request.setTourName("Tour de Ejemplo");
        request.setDescription("Este es un tour generado por el test");
        request.setMaterialColors(Map.of("pared", "#ffffff", "techo", "#000000"));
        request.setSteps(List.of(step));

        // Usuario mock
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("creator@example.com");

        when(userRepository.findByUsernameOrEmail("creator@example.com", "creator@example.com"))
                .thenReturn(Optional.of(mockUser));

        // Paso convertido a entidad
        Tour.Step convertedStep = new Tour.Step();
        convertedStep.setStepId(step.getStepId());
        convertedStep.setMessages(step.getMessages());

        Tour.BoardMedia convertedBoardMedia = new Tour.BoardMedia();
        convertedBoardMedia.setHtml(step.getBoardMedia().getHtml());
        convertedStep.setBoardMedia(convertedBoardMedia);

        // Tour final
        Tour savedTour = new Tour();
        savedTour.setTourId("abc123");
        savedTour.setTourName(request.getTourName());
        savedTour.setDescription(request.getDescription());
        savedTour.setMaterialColors(request.getMaterialColors());
        savedTour.setSteps(List.of(convertedStep));
        savedTour.setTemplateId(new ObjectId(request.getTemplateId()));
        savedTour.setUserId(userId);

        when(tourService.createTour(any(Tour.class), any(ObjectId.class))).thenReturn(savedTour);

        mockMvc.perform(post("/api/tours/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tourName").value("Tour de Ejemplo"))
                .andExpect(jsonPath("$.description").value("Este es un tour generado por el test"))
                .andExpect(jsonPath("$.materialColors.pared").value("#ffffff"))
                .andExpect(jsonPath("$.materialColors.techo").value("#000000"))
                .andExpect(jsonPath("$.steps[0].stepId").value("paso-1"))
                .andExpect(jsonPath("$.steps[0].boardMedia.html").value("<p>Contenido interactivo</p>"));
    }
}
