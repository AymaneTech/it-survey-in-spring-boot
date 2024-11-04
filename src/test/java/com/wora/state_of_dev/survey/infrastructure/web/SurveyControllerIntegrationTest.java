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
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.wora.state_of_dev.common.infrastructure.web.GlobalExceptionHandler.ENTITY_NOT_FOUND_MESSAGE;
import static com.wora.state_of_dev.common.infrastructure.web.GlobalExceptionHandler.VALIDATION_FAILED_MESSAGE;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class SurveyControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final SurveyService surveyService;
    private final OwnerService ownerService;

    private OwnerResponseDto owner;
    private SurveyResponseDto createdSurvey;

    @BeforeEach
    void setUp() {
        owner = ownerService.create(new OwnerRequestDto("aymane the boss"));
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
                .andExpect(jsonPath("$[0].title").value(createdSurvey.title()));
    }

    @Nested
    class FindByIdTests {
        @Test
        @Rollback
        void givenNotExistentSurveyId_whenFindById_shouldReturnNotFound() throws Exception {
            mockMvc.perform(get("/api/v1/surveys/{id}", 303L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }

        @Test
        @Rollback
        void givenExistingSurveyId_whenFindById_shouldReturnFoundSurvey() throws Exception {
            mockMvc.perform(get("/api/v1/surveys/{id}", createdSurvey.id()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(createdSurvey.id()))
                    .andExpect(jsonPath("$.title").value(createdSurvey.title()));
        }
    }

    @Nested
    class CreateTests {
        @Test
        @Rollback
        void givenCorrectRequest_whenCreate_shouldReturnCreatedSurvey() throws Exception {
            SurveyRequestDto surveyRequest = new SurveyRequestDto("new survey motherfuckers", "description", owner.id());
            mockMvc.perform(post("/api/v1/surveys")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(surveyRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").value(surveyRequest.title()));
        }

        @Test
        @Rollback
        void givenNotExistentOwnerId_whenCreate_shouldReturnNotFound() throws Exception {
            SurveyRequestDto surveyRequest = new SurveyRequestDto("new survey motherfuckers", "description", 393L);
            mockMvc.perform(post("/api/v1/surveys")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(surveyRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }

        @Test
        @Rollback
        void givenInvalidRequest_whenCreate_shouldReturnBadRequest() throws Exception {
            SurveyRequestDto surveyRequest = new SurveyRequestDto("", "", owner.id());
            mockMvc.perform(post("/api/v1/surveys")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(surveyRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED_MESSAGE));
        }
    }

    @Nested
    class UpdateTests {
        @Test
        @Rollback
        void givenInvalidRequest_whenUpdate_shouldReturnBadRequest() throws Exception {
            SurveyRequestDto surveyRequest = new SurveyRequestDto("", "", owner.id());
            mockMvc.perform(put("/api/v1/surveys/{id}", createdSurvey.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(surveyRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400));
        }

        @Test
        @Rollback
        void givenNotExistentOwnerId_whenUpdate_shouldReturnBadRequest() throws Exception {
            SurveyRequestDto surveyRequest = new SurveyRequestDto("valid data", "valid description", 293L);
            mockMvc.perform(put("/api/v1/surveys/{id}", createdSurvey.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(surveyRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(404));
        }

        @Test
        @Rollback
        void givenCorrectRequest_whenUpdate_shouldReturnUpdatedSurvey() throws Exception {
            SurveyRequestDto surveyRequest = new SurveyRequestDto("valid data", "valid description", owner.id());
            mockMvc.perform(put("/api/v1/surveys/{id}", createdSurvey.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(surveyRequest)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class DeleteTests {
        @Test
        @Rollback
        void givenNotExistentId_whenDelete_shouldReturnNotFound() throws Exception {
            mockMvc.perform(delete("/api/v1/surveys/{id}", 93939L))
                    .andExpect(status().isNotFound());
        }

        @Test
        @Rollback
        void givenExistingId_whenDelete_shouldDeleteSurvey() throws Exception {
            mockMvc.perform(delete("/api/v1/surveys/{id}", createdSurvey.id()))
                    .andExpect(status().isNoContent());
        }
    }
}