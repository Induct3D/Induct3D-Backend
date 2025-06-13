package com.upao.induct3d.backend.Controller;

import com.upao.induct3d.backend.entity.Template;
import com.upao.induct3d.backend.jwt.JwtProvider;
import com.upao.induct3d.backend.repository.TemplateRepository;
import com.upao.induct3d.backend.repository.UserRepository;
import com.upao.induct3d.backend.service.TemplateService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TemplateControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private TemplateService templateService;
    @MockBean private UserRepository userRepository;
    @MockBean private TemplateRepository templateRepository;
    @MockBean private JwtProvider jwtProvider;

    @Test
    @DisplayName("T1: Debería devolver una lista de templates")
    public void shouldReturnTemplateList() throws Exception {
        Template template = new Template();
        template.setId(new ObjectId().toHexString()); // ← Solución aquí
        template.setName("Plantilla Ejemplo");
        template.setDescription("Una descripción");
        template.setGlbUrl("https://example.com/model.glb");

        when(templateService.getTemplates()).thenReturn(List.of(template));

        mockMvc.perform(get("/api/templates")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Plantilla Ejemplo"))
                .andExpect(jsonPath("$[0].glbUrl").value("https://example.com/model.glb"));
    }
}

