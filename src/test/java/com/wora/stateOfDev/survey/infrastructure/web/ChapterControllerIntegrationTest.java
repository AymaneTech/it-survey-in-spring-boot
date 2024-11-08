package com.wora.stateOfDev.survey.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wora.stateOfDev.owner.application.dto.OwnerRequestDto;
import com.wora.stateOfDev.owner.application.service.OwnerService;
import com.wora.stateOfDev.survey.application.dto.request.ChapterRequestDto;
import com.wora.stateOfDev.survey.application.dto.request.SurveyEditionRequestDto;
import com.wora.stateOfDev.survey.application.dto.request.SurveyRequestDto;
import com.wora.stateOfDev.survey.application.dto.response.ChapterResponseDto;
import com.wora.stateOfDev.survey.application.dto.response.SurveyEditionResponseDto;
import com.wora.stateOfDev.survey.application.service.ChapterService;
import com.wora.stateOfDev.survey.application.service.SurveyEditionService;
import com.wora.stateOfDev.survey.application.service.SurveyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Year;

import static com.wora.stateOfDev.common.infrastructure.web.GlobalExceptionHandler.ENTITY_NOT_FOUND_MESSAGE;
import static com.wora.stateOfDev.common.infrastructure.web.GlobalExceptionHandler.VALIDATION_FAILED_MESSAGE;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ChapterControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ChapterService chapterService;
    private final SurveyEditionService surveyEditionService;
    private final SurveyService surveyService;
    private final OwnerService ownerService;

    private ChapterResponseDto chapter;
    private SurveyEditionResponseDto surveyEdition;

    @BeforeEach
    void setup() {
        Long ownerId = ownerService.create(new OwnerRequestDto("aymane el maini")).id();
        Long surveyId = surveyService.create(new SurveyRequestDto("hello motherfuckers", "description ", ownerId)).id();
        surveyEdition = surveyEditionService.create(new SurveyEditionRequestDto(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(39),
                Year.now(),
                surveyId)
        );
        chapter = chapterService.create(new ChapterRequestDto("web developement", surveyEdition.id(), null));
    }

    @Nested
    class CreateTests {
        @Test
        @Rollback
        void givenCorrectRequestAndExistentSurveyEditionId_whenCreate_shouldCreateAndReturnChapter() throws Exception {
            ChapterRequestDto request = new ChapterRequestDto("web dev chapter", surveyEdition.id(), null);
            mockMvc.perform(post("/api/v1/chapters")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").value(request.title()));
        }

        @Test
        @Rollback
        void givenCorrectRequestAndInvalidSurveyEditionId_whenCreate_shouldReturnNotFound() throws Exception {
            ChapterRequestDto request = new ChapterRequestDto("web dev chapter", 293939L, null);
            mockMvc.perform(post("/api/v1/chapters" )
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED_MESSAGE))
                    .andDo(print());
        }

        @Test
        @Rollback
        void givenInvalidRequsetAndCorrectId_whenCreate_shouldReturnBadRequest() throws Exception {
            ChapterRequestDto request = new ChapterRequestDto("", null, null); // this is invalid request

            mockMvc.perform(post("/api/v1/chapters", surveyEdition.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED_MESSAGE));
        }
    }

    @Nested
    class FindBySurveyEditionId {
        @Test
        @Rollback
        void givenCorrectSurveyEditionId_whenFindBySurveyEditionId_shouldReturnChapters() throws Exception {
            mockMvc.perform(get("/api/v1/survey-editions/{id}/chapters", surveyEdition.id()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));
        }

        @Test
        @Rollback
        void givenNotExistentSurveyEditionId_whenFindBySurveyEditionId_shouldReturnNotFound() throws Exception {
            mockMvc.perform(get("/api/v1/survey-editions/{id}/chapters", 9393L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }
    }

    @Nested
    class FindById {
        @Test
        @Rollback
        void givenExistingChapterId_whenFindById_shouldReturnFoundChapter() throws Exception {
            mockMvc.perform(get("/api/v1/chapters/{id}", chapter.id()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(chapter.title()));
        }

        @Test
        @Rollback
        void givenNotExistentChapterId_whenFindById_shouldReturnNotFound() throws Exception {
            mockMvc.perform(get("/api/v1/chapters/{id}", 93939L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(404));
        }
    }

    @Nested
    class UpdateTests {
        @Test
        @Rollback
        void givenExistentIdAndCorrectRequest_whenUpdate_shouldReturnUpdateChapter() throws Exception {
            ChapterRequestDto request = new ChapterRequestDto("udpate shit", surveyEdition.id(), null);

            mockMvc.perform(put("/api/v1/chapters/{id}", chapter.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(request.title()));
        }
    }

    @Nested
    class DeleteTests {
        @Test
        @Rollback
        void givenNotExistentId_whenDelete_shouldReturnNotFound() throws Exception {
            mockMvc.perform(delete("/api/v1/chapters/{id}", 93939L))
                    .andExpect(status().isNotFound());
        }

        @Test
        @Rollback
        void givenExistingId_whenDelete_shouldDeleteSurvey() throws Exception {
            mockMvc.perform(delete("/api/v1/chapters/{id}", chapter.id()))
                    .andExpect(status().isNoContent());
        }
    }
}
