package com.wora.state_of_dev.survey.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wora.state_of_dev.common.infrastructure.web.GlobalExceptionHandler;
import com.wora.state_of_dev.owner.application.dto.OwnerRequestDto;
import com.wora.state_of_dev.owner.application.service.OwnerService;
import com.wora.state_of_dev.survey.application.dto.request.*;
import com.wora.state_of_dev.survey.application.dto.response.ChapterResponseDto;
import com.wora.state_of_dev.survey.application.dto.response.QuestionResponseDto;
import com.wora.state_of_dev.survey.application.service.ChapterService;
import com.wora.state_of_dev.survey.application.service.QuestionService;
import com.wora.state_of_dev.survey.application.service.SurveyEditionService;
import com.wora.state_of_dev.survey.application.service.SurveyService;
import com.wora.state_of_dev.survey.domain.valueObject.AnswerType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class SurveySubmissionControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final QuestionService questionService;
    private final ChapterService chapterService;
    private final SurveyEditionService surveyEditionService;
    private final SurveyService surveyService;
    private final OwnerService ownerService;

    private QuestionResponseDto question1;
    private QuestionResponseDto question2;
    private Long surveyEditionId;

    @BeforeEach
    void setup() {
        Long ownerId = ownerService.create(new OwnerRequestDto("aymane el Maini")).id();
        Long surveyId = surveyService.create(new SurveyRequestDto("state of motherfuckers", "state of description", ownerId)).id();
        surveyEditionId = surveyEditionService.create(new SurveyEditionRequestDto(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(39),
                Year.now(),
                surveyId)
        ).id();
        ChapterResponseDto chapter = chapterService.create(new ChapterRequestDto("web development", surveyEditionId, null));
        question1 = questionService.create(new QuestionRequestDto("your best framework", chapter.id(), AnswerType.SINGLE_CHOICE, List.of(
                        new AnswerRequestDto("spring boot"),
                        new AnswerRequestDto("laravel"),
                        new AnswerRequestDto("nest js")
                ))
        );

        question2 = questionService.create(new QuestionRequestDto("tatata", chapter.id(), AnswerType.MULTI_CHOICE, List.of(
                new AnswerRequestDto("spring boot"),
                new AnswerRequestDto("laravel"),
                new AnswerRequestDto("nest js")
        )));

    }

    @Test
    @Rollback
    void givenSingleSubmissionRequest_whenParticipate_shouldReturnNoContent() throws Exception {
        final String requestJson = String.format("""
                {
                    "questionId": %s,
                    "answer": %s
                }
                """, question1.id(), question1.answers().getFirst().id());
        mockMvc.perform(post("/api/v1/survey-editions/{id}/participate", surveyEditionId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isNoContent());
    }

    @Test
    @Rollback
    void givenMultipleSubmissionRequest_whenParticipate_shouldReturnNoContent() throws Exception {
        Long a1 = question2.answers().getFirst().id();
        Long a2 = question2.answers().getLast().id();

        /*
     {
	"submissions": [
		{
			"questionId": 52,
			"answer": [102]
		}
	]
}
        * */

        final String requestJson = String.format("""
                        {
                            "submissions": [
                                {
                                    "questionId": %s,
                                    "answer": %s
                                },
                                {
                                    "questionId": %s,
                                    "answer": [%s, %s]
                                }
                            ]
                        }
                        """, question1.id(), question1.answers().getFirst().id(),
                question2.id(), a1, a2);
        mockMvc.perform(post("/api/v1/survey-editions/{id}/participate", surveyEditionId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isNoContent());
    }

}