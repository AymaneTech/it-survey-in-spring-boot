package com.wora.stateOfDev.survey.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wora.stateOfDev.owner.application.dto.OwnerRequestDto;
import com.wora.stateOfDev.owner.application.service.OwnerService;
import com.wora.stateOfDev.survey.application.dto.request.*;
import com.wora.stateOfDev.survey.application.dto.response.ChapterResponseDto;
import com.wora.stateOfDev.survey.application.dto.response.QuestionResponseDto;
import com.wora.stateOfDev.survey.application.dto.response.SurveyEditionResponseDto;
import com.wora.stateOfDev.survey.application.service.ChapterService;
import com.wora.stateOfDev.survey.application.service.QuestionService;
import com.wora.stateOfDev.survey.application.service.SurveyEditionService;
import com.wora.stateOfDev.survey.application.service.SurveyService;
import com.wora.stateOfDev.survey.domain.valueObject.AnswerType;
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
import java.util.List;

import static com.wora.stateOfDev.common.infrastructure.web.GlobalExceptionHandler.ENTITY_NOT_FOUND_MESSAGE;
import static com.wora.stateOfDev.common.infrastructure.web.GlobalExceptionHandler.VALIDATION_FAILED_MESSAGE;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class QuestionControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final QuestionService questionService;
    private final ChapterService chapterService;
    private final SurveyEditionService surveyEditionService;
    private final SurveyService surveyService;
    private final OwnerService ownerService;

    private QuestionResponseDto question;
    private SurveyEditionResponseDto surveyEdition;
    private ChapterResponseDto chapter;
    private List<AnswerRequestDto> answerRequestDtos;

    @BeforeEach
    void setup() {
        Long ownerId = ownerService.create(new OwnerRequestDto("aymane el Maini")).id();
        Long surveyId = surveyService.create(new SurveyRequestDto("state of motherfuckers", "state of description", ownerId)).id();
        surveyEdition = surveyEditionService.create(new SurveyEditionRequestDto(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(39),
                Year.now(),
                surveyId)
        );
        chapter = chapterService.create(new ChapterRequestDto("web developement", surveyEdition.id(), null));
        question = questionService.create(new QuestionRequestDto("your best framework", chapter.id(), AnswerType.SINGLE_CHOICE, List.of(
                new AnswerRequestDto("spring boot"), new AnswerRequestDto("laravel"), new AnswerRequestDto("quarkus")
        )));

        answerRequestDtos = List.of(
                new AnswerRequestDto("spring boot"),
                new AnswerRequestDto("laravel"),
                new AnswerRequestDto("nest js")
        );
    }

    @Test
    @Rollback
    void givenQuestionsList_whenFindAll_shouldReturnQuestionResponseList() throws Exception {
        mockMvc.perform(get("/api/v1/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].text").value(question.text()));
    }

    @Nested
    class FindByIdTests {
        @Test
        @Rollback
        void givenQuestionIdDoesNotExists_whenFindById_shouldReturnNotFound() throws Exception {
            mockMvc.perform(get("/api/v1/questions/{id}", 3389L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }

        @Test
        @Rollback
        void givenExistentQuestionId_whenFindById_shouldReturnFoundQuestion() throws Exception {
            mockMvc.perform(get("/api/v1/questions/{id}", question.id()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.text").value(question.text()));
        }
    }

    @Nested
    class CreateTests {
        @Test
        @Rollback
        void givenChapterIdDoesNotExists_whenCreate_shouldReturnBadRequest() throws Exception {
            QuestionRequestDto invalidRequest = new QuestionRequestDto("your stack", 3939L, AnswerType.SINGLE_CHOICE, answerRequestDtos);

            mockMvc.perform(post("/api/v1/questions")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED_MESSAGE))
                    .andExpect(jsonPath("$.code").value(400));
        }

        @Test
        @Rollback
        void givenChapterThatHasSubChapters_whenCreate_shouldReturnChapterHasSubChaptersException() throws Exception {
            chapterService.create(new ChapterRequestDto("sub chapter", surveyEdition.id(), chapter.id()));
            QuestionRequestDto invalidRequest = new QuestionRequestDto("your stack", chapter.id(), AnswerType.SINGLE_CHOICE, answerRequestDtos);

            mockMvc.perform(post("/api/v1/questions")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.errors").value("Cannot create question with chapter that has sub chapters"))
                    .andExpect(jsonPath("$.code").value(500));
        }

        @Test
        @Rollback
        void givenInvalidRequest_whenCreate_shouldReturnValidationFailed() throws Exception {
            QuestionRequestDto invalidRequest = new QuestionRequestDto("", chapter.id(), AnswerType.SINGLE_CHOICE, answerRequestDtos);

            mockMvc.perform(post("/api/v1/questions")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED_MESSAGE))
                    .andExpect(jsonPath("$.errors.text").value("must not be blank"));

        }

        @Test
        @Rollback
        void givenCorrectRequestAndEmptyAnswersList_whenCreate_shouldReturnAnswersCannotBeEmpty() throws Exception {
            QuestionRequestDto invalidRequest = new QuestionRequestDto("what is spring ", chapter.id(), AnswerType.SINGLE_CHOICE, List.of());

            mockMvc.perform(post("/api/v1/questions")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value("cannot create question without its answers"));
        }

        @Test
        @Rollback
        void givenCorrectRequest_whenCreate_shouldReturnCreatedAnswer() throws Exception {
            QuestionRequestDto request = new QuestionRequestDto("what is spring ", chapter.id(), AnswerType.SINGLE_CHOICE, answerRequestDtos);

            mockMvc.perform(post("/api/v1/questions")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    class UpdateTests {
        @Test
        @Rollback
        void givenChapterIdDoesNotExists_whenUpdate_shouldReturnNotFound() throws Exception {
            QuestionRequestDto invalidRequest = new QuestionRequestDto("your stack", 3939L, AnswerType.SINGLE_CHOICE, answerRequestDtos);

            mockMvc.perform(put("/api/v1/questions/{id}", question.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED_MESSAGE))
                    .andExpect(jsonPath("$.code").value(400));
        }

        @Test
        @Rollback
        void givenChapterThatHasSubChapters_whenUpdate_shouldReturnChapterHasSubChaptersException() throws Exception {
            chapterService.create(new ChapterRequestDto("sub chapter", surveyEdition.id(), chapter.id()));
            QuestionRequestDto invalidRequest = new QuestionRequestDto("your stack", chapter.id(), AnswerType.SINGLE_CHOICE, answerRequestDtos);

            mockMvc.perform(put("/api/v1/questions/{id}", question.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.errors").value("Cannot create question with chapter that has sub chapters"))
                    .andExpect(jsonPath("$.code").value(500));
        }

        @Test
        @Rollback
        void givenInvalidRequest_whenUpdate_shouldReturnValidationFailed() throws Exception {
            QuestionRequestDto invalidRequest = new QuestionRequestDto("", chapter.id(), AnswerType.SINGLE_CHOICE, answerRequestDtos);

            mockMvc.perform(put("/api/v1/questions/{id}", question.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED_MESSAGE))
                    .andExpect(jsonPath("$.errors.text").value("must not be blank"));

        }

        @Test
        @Rollback
        void givenCorrectRequestAndEmptyAnswersList_whenUpdate_shouldReturnAnswersCannotBeEmpty() throws Exception {
            QuestionRequestDto invalidRequest = new QuestionRequestDto("what is spring ", chapter.id(), AnswerType.SINGLE_CHOICE, List.of());

            mockMvc.perform(put("/api/v1/questions/{id}", question.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value("cannot create question without its answers"));
        }
        @Test
        @Rollback
        void givenNotExistentQuestionId_whenUpdate_shouldReturnUpdatedAnswer() throws Exception {
            QuestionRequestDto request = new QuestionRequestDto("what is spring ", chapter.id(), AnswerType.SINGLE_CHOICE, answerRequestDtos);

            mockMvc.perform(put("/api/v1/questions/{id}", 39393L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }

        @Test
        @Rollback
        void givenCorrectRequest_whenUpdate_shouldReturnCreatedAnswer() throws Exception {
            QuestionRequestDto request = new QuestionRequestDto("what is spring ", chapter.id(), AnswerType.SINGLE_CHOICE, answerRequestDtos);

            mockMvc.perform(put("/api/v1/questions/{id}", question.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class DeleteTests {
        @Test
        @Rollback
        void givenNotExistentId_whenDelete_shouldReturnNotFound() throws Exception {
            mockMvc.perform(delete("/api/v1/questions/{id}", 93939L))
                    .andExpect(status().isNotFound());
        }

        @Test
        @Rollback
        void givenExistingId_whenDelete_shouldDeleteSurvey() throws Exception {
            mockMvc.perform(delete("/api/v1/questions/{id}", question.id()))
                    .andExpect(status().isNoContent());
        }
    }
}