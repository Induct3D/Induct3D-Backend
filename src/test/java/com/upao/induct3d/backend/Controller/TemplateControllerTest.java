package com.upao.induct3d.backend.Controller;

import com.upao.induct3d.backend.domain.response.TemplateResponse;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        TemplateResponse templateResponse = new TemplateResponse();
        templateResponse.setId(new ObjectId().toHexString());
        templateResponse.setName("Plantilla Ejemplo");
        templateResponse.setDescription("Una descripción");
        templateResponse.setGlbUrl("https://example.com/model.glb");

        when(templateService.getTemplates()).thenReturn(List.of(templateResponse));

        mockMvc.perform(get("/api/templates")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

