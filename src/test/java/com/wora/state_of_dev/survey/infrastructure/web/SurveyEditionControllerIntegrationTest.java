package com.wora.state_of_dev.survey.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wora.state_of_dev.common.infrastructure.web.GlobalExceptionHandler;
import com.wora.state_of_dev.owner.application.dto.OwnerRequestDto;
import com.wora.state_of_dev.owner.application.dto.OwnerResponseDto;
import com.wora.state_of_dev.owner.application.service.OwnerService;
import com.wora.state_of_dev.survey.application.dto.request.SurveyEditionRequestDto;
import com.wora.state_of_dev.survey.application.dto.request.SurveyRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SurveyEditionResponseDto;
import com.wora.state_of_dev.survey.application.dto.response.SurveyResponseDto;
import com.wora.state_of_dev.survey.application.service.SurveyEditionService;
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

import java.time.LocalDateTime;
import java.time.Year;

import static com.wora.state_of_dev.common.infrastructure.web.GlobalExceptionHandler.ENTITY_NOT_FOUND;
import static com.wora.state_of_dev.common.infrastructure.web.GlobalExceptionHandler.VALIDATION_FAILED;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class SurveyEditionControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final SurveyEditionService surveyEditionService;
    private final SurveyService surveyService;
    private final OwnerService ownerService;

    private SurveyEditionRequestDto surveyEditionRequestDto;
    private SurveyEditionResponseDto editionDto;
    private SurveyResponseDto surveyResponseDto;

    @BeforeEach
    void setup() {
        OwnerResponseDto owner = ownerService.create(new OwnerRequestDto("aymane the bosss"));
        surveyResponseDto = surveyService.create(new SurveyRequestDto("state of motherfuckers", "state of motherfuckers in morocco", owner.id()));
        surveyEditionRequestDto = new SurveyEditionRequestDto(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(40), Year.now(), surveyResponseDto.id());
        editionDto = surveyEditionService.create(surveyEditionRequestDto);
    }

    @Test
    @Rollback
    void givenSurveyEditionsExists_whenFindAll_shouldReturnSurveyEditionsList() throws Exception {
        mockMvc.perform(get("/api/v1/survey-editions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id").value(editionDto.id()))
                .andDo(print());
    }

    @Nested
    class FindById {
        @Test
        @Rollback
        void givenSurveyEditionIdExists_whenFindById_shouldReturnFoundSurveyEdition() throws Exception {
            mockMvc.perform(get("/api/v1/survey-editions/{id}", editionDto.id()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(editionDto.id()))
                    .andDo(print());
        }

        @Test
        @Rollback
        void givenSurveyEditionDoesNotExists_whenFindById_shouldReturnNotFound() throws Exception {
            mockMvc.perform(get("/api/v1/survey-editions/{id}", 939L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(404))

                    .andDo(print());
        }
    }

    @Nested
    class CreateTests {
        @Test
        @Rollback
        void givenInvalidRequest_whenCreate_shouldReturnBadRequest() throws Exception {
            SurveyEditionRequestDto invalidRequest = new SurveyEditionRequestDto(LocalDateTime.now().minusYears(4), null, Year.now(), surveyResponseDto.id());

            mockMvc.perform(post("/api/v1/survey-editions")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED))
                    .andDo(print());
        }

        @Test
        @Rollback
        void givenNotExistentSurveyId_whenCreate_shouldReturnNotFound() throws Exception {
            SurveyEditionRequestDto invalidRequest = new SurveyEditionRequestDto(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(39), Year.now(), 9393L);

            mockMvc.perform(post("/api/v1/survey-editions")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND))
                    .andDo(print());
        }

        @Test
        @Rollback
        void givenCorrectRequest_whenCreate_shouldCreateAndReturnSurveyEdition() throws Exception {
            SurveyEditionRequestDto request = new SurveyEditionRequestDto(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(39), Year.now(), surveyResponseDto.id());

            mockMvc.perform(post("/api/v1/survey-editions")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.survey.id").value(surveyResponseDto.id()))
                    .andDo(print());
        }
    }

    @Nested
    class UpdateTests {
        @Test
        @Rollback
        void givenInvalidRequest_whenUpdate_shouldReturnBadRequest() throws Exception {
            SurveyEditionRequestDto invalidRequest = new SurveyEditionRequestDto(LocalDateTime.now().minusYears(4), null, Year.now(), surveyResponseDto.id());

            mockMvc.perform(put("/api/v1/survey-editions/{id}", surveyResponseDto.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED))
                    .andDo(print());
        }

        @Test
        @Rollback
        void givenNotExistentSurveyId_whenUpdate_shouldReturnNotFound() throws Exception {
            SurveyEditionRequestDto validRequest = new SurveyEditionRequestDto(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(39), Year.now(), 9393L);

            mockMvc.perform(put("/api/v1/survey-editions/{id}", 9999L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND))
                    .andDo(print());
        }

        @Test
        @Rollback
        void givenCorrectRequest_whenUpdate_shouldUpdateAndReturnSurveyEdition() throws Exception {
            SurveyEditionRequestDto validRequest = new SurveyEditionRequestDto(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(39), Year.now(), surveyResponseDto.id());

            mockMvc.perform(put("/api/v1/survey-editions/{id}", editionDto.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Nested
    class DeleteTests {
        @Test
        @Rollback
        void givenNotExistentId_whenDelete_shouldReturnNotFound() throws Exception {
            mockMvc.perform(delete("/api/v1/survey-editions/{id}", 93939L))
                    .andExpect(status().isNotFound());
        }

        @Test
        @Rollback
        void givenExistingId_whenDelete_shouldDeleteSurveyEdition() throws Exception {
            mockMvc.perform(delete("/api/v1/survey-editions/{id}", editionDto.id()))
                    .andExpect(status().isNoContent());
        }
    }

}