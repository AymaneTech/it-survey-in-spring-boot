package com.wora.state_of_dev.survey.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wora.state_of_dev.common.infrastructure.web.GlobalExceptionHandler;
import com.wora.state_of_dev.owner.application.dto.OwnerRequestDto;
import com.wora.state_of_dev.owner.application.dto.OwnerResponseDto;
import com.wora.state_of_dev.owner.application.service.OwnerService;
import com.wora.state_of_dev.survey.application.dto.request.SurveyRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SurveyResponseDto;
import com.wora.state_of_dev.survey.application.service.SurveyService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
@Transactional
class SurveyControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final SurveyService surveyService;
    private final OwnerService ownerService;

    private SurveyResponseDto createdSurvey;

    @Autowired
    public SurveyControllerIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper, SurveyService surveyService, OwnerService ownerService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.surveyService = surveyService;
        this.ownerService = ownerService;
    }

    @BeforeEach
    void setUp() {
        OwnerResponseDto owner = ownerService.create(new OwnerRequestDto("aymane the boss"));
        SurveyRequestDto validDto = new SurveyRequestDto("state of motherfuckers", "state of mother fuckers in morocco", owner.id());
        createdSurvey = surveyService.create(validDto);
    }

    @Test
    @Rollback
    void givenSurveysExist_whenFindAll_shouldReturnListOfSurveys() throws Exception {
        mockMvc.perform(get("/api/v1/surveys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(createdSurvey.id()))
                .andExpect(jsonPath("$[0].title").value(createdSurvey.title()))
                .andDo(print());
    }

    @Nested
    class FindByIdTests {
        @Test
        @Rollback
        void givenNotExistentSurveyId_whenFindById_shouldReturnNotFound() throws Exception {
            mockMvc.perform(get("/api/v1/surveys/{id}", 303L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value("resource you are looking for not found"))
                    .andDo(print());
        }
    }
}