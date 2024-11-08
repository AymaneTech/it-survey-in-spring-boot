package com.wora.state_of_dev.survey.application.service;

import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.survey.application.dto.embeddable.SurveyEmbeddableDto;
import com.wora.state_of_dev.survey.application.dto.request.SurveyEditionRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SurveyEditionResponseDto;
import com.wora.state_of_dev.survey.application.mapper.SurveyEditionMapper;
import com.wora.state_of_dev.survey.application.service.impl.DefaultSurveyEditionService;
import com.wora.state_of_dev.survey.domain.entity.Survey;
import com.wora.state_of_dev.survey.domain.entity.SurveyEdition;
import com.wora.state_of_dev.survey.domain.repository.SurveyEditionRepository;
import com.wora.state_of_dev.survey.domain.repository.SurveyRepository;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Default survey edition service tests")
@ExtendWith(MockitoExtension.class)
class DefaultSurveyEditionServiceTest {

    @Mock
    private SurveyEditionRepository repository;
    @Mock
    private SurveyRepository surveyRepository;
    @Mock
    private SurveyEditionMapper mapper;
    @InjectMocks
    private DefaultSurveyEditionService sut;

    private SurveyEdition surveyEdition;
    private Survey survey;

    @BeforeEach
    void setup() {
        survey = new Survey("state of motherfuckers", "state of motherfuckers in morocco statistics")
                .setId(new SurveyId(1L));
        surveyEdition = new SurveyEdition(LocalDateTime.now(), LocalDateTime.now().plusDays(4), Year.of(2024))
                .setId(new SurveyEditionId(5L))
                .setSurvey(survey);
    }

    @Nested
    class FindAll {
        @Test
        void given_surveyEditionsExists_whenFindAll_shouldReturnSurveyEditionsList() {
            SurveyEdition surveyEdition1 = new SurveyEdition(LocalDateTime.now().plusMonths(2), LocalDateTime.now().plusMonths(4), Year.of(2024))
                    .setId(new SurveyEditionId(5L))
                    .setSurvey(survey);
            List<SurveyEdition> expected = List.of(surveyEdition, surveyEdition1);

            given(repository.findAll()).willReturn(expected);
            given(mapper.toResponseDto(any(SurveyEdition.class)))
                    .willAnswer(invocation -> {
                        SurveyEdition arg = invocation.getArgument(0);
                        SurveyEmbeddableDto surveyDto = new SurveyEmbeddableDto(arg.getSurvey().getId().value(), arg.getSurvey().getTitle(), arg.getSurvey().getDescription(), null);
                        return new SurveyEditionResponseDto(arg.getId().value(), arg.getStartDate(), arg.getEndDate(), arg.getYear(), surveyDto);
                    });

            List<SurveyEditionResponseDto> actual = sut.findAll();

            assertThat(actual.size()).isEqualTo(2);
            assertThat(actual).isNotNull();
            verify(repository).findAll();
            verify(mapper, times(2)).toResponseDto(any(SurveyEdition.class));
        }

        @Test
        void given_surveysDoesNotExists_whenFindAll_shouldReturnEmptyList() {
            given(repository.findAll()).willReturn(List.of());

            List<SurveyEditionResponseDto> actual = sut.findAll();

            assertThat(actual.isEmpty()).isTrue();
            verify(repository).findAll();
            verify(mapper, never()).toResponseDto(any(SurveyEdition.class));
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        void given_surveyEditionIdExists_whenFindById_shouldReturnSurveyEdition() {
            given(repository.findById(any(SurveyEditionId.class))).willReturn(Optional.of(surveyEdition));
            given(mapper.toResponseDto(any(SurveyEdition.class)))
                    .willAnswer(invocation -> {
                        SurveyEdition arg = invocation.getArgument(0);
                        SurveyEmbeddableDto surveyDto = new SurveyEmbeddableDto(arg.getSurvey().getId().value(), arg.getSurvey().getTitle(), arg.getSurvey().getDescription(), null);
                        return new SurveyEditionResponseDto(arg.getId().value(), arg.getStartDate(), arg.getEndDate(), arg.getYear(), surveyDto);
                    });

            SurveyEditionResponseDto actual = sut.findById(new SurveyEditionId(5L));

            assertThat(actual.id()).isEqualTo(surveyEdition.getId().value());
            assertThat(actual.year()).isEqualTo(surveyEdition.getYear());
            verify(repository).findById(any(SurveyEditionId.class));
            verify(mapper).toResponseDto(any(SurveyEdition.class));
        }

        @Test
        void given_surveyEditionDoesNotExist_whenFindById_shouldThrowEntityNotFoundException() {
            given(repository.findById(any(SurveyEditionId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.findById(new SurveyEditionId(2L)))
                    .withMessageContaining("survey edition   with id 2 not found");
        }
    }

    @Nested
    class CreateTests {
        @Test
        void given_surveyEditionRequestDto_whenCreate_shouldCreateAndReturnSurveyEdition() {
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = startDate.plusDays(4);
            SurveyEditionRequestDto requestDto = new SurveyEditionRequestDto(startDate, endDate, Year.of(2024), 1L);

            given(surveyRepository.findById(any(SurveyId.class))).willReturn(Optional.of(survey));
            given(mapper.toEntity(eq(requestDto))).willReturn(surveyEdition);
            given(repository.save(any(SurveyEdition.class))).willReturn(surveyEdition);
            given(mapper.toResponseDto(any(SurveyEdition.class)))
                    .willAnswer(invocation -> {
                        SurveyEdition arg = invocation.getArgument(0);
                        SurveyEmbeddableDto surveyDto = new SurveyEmbeddableDto(arg.getSurvey().getId().value(), arg.getSurvey().getTitle(), arg.getSurvey().getDescription(), null);
                        return new SurveyEditionResponseDto(arg.getId().value(), arg.getStartDate(), arg.getEndDate(), arg.getYear(), surveyDto);
                    });

            SurveyEditionResponseDto actual = sut.create(requestDto);

            assertThat(actual).isNotNull();
            assertThat(actual.year()).isEqualTo(requestDto.year());
            verify(surveyRepository).findById(any(SurveyId.class));
            verify(repository).save(any(SurveyEdition.class));
        }

        @Test
        void given_surveyDoesNotExist_whenCreate_shouldThrowEntityNotFoundException() {
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = startDate.plusDays(4);
            SurveyEditionRequestDto requestDto = new SurveyEditionRequestDto(startDate, endDate, Year.of(2024), 1L);

            given(surveyRepository.findById(any(SurveyId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.create(requestDto))
                    .withMessageContaining("survey with id 1 not found");
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void given_surveyEditionDoesNotExist_whenUpdate_shouldThrowEntityNotFoundException() {
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = startDate.plusDays(4);
            SurveyEditionRequestDto requestDto = new SurveyEditionRequestDto(startDate, endDate, Year.of(2024), 1L);

            given(repository.findById(any(SurveyEditionId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.update(new SurveyEditionId(4L), requestDto))
                    .withMessageContaining("survey edition with id 4 not found");
        }

        @Test
        void given_surveyDoesNotExist_whenUpdate_shouldThrowEntityNotFoundException() {
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = startDate.plusDays(4);
            SurveyEditionRequestDto requestDto = new SurveyEditionRequestDto(startDate, endDate, Year.of(2024), 1L);

            given(repository.findById(any(SurveyEditionId.class))).willReturn(Optional.of(surveyEdition));
            given(surveyRepository.findById(any(SurveyId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.update(new SurveyEditionId(5L), requestDto))
                    .withMessageContaining("survey with id 1 not found");
        }

        @Test
        void given_validRequest_whenUpdate_shouldUpdateAndReturnSurveyEdition() {
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = startDate.plusDays(4);
            SurveyEditionRequestDto requestDto = new SurveyEditionRequestDto(startDate, endDate, Year.of(2025), 1L);

            given(repository.findById(any(SurveyEditionId.class))).willReturn(Optional.of(surveyEdition));
            given(surveyRepository.findById(any(SurveyId.class))).willReturn(Optional.of(survey));
            given(mapper.toResponseDto(any(SurveyEdition.class)))
                    .willAnswer(invocation -> {
                        SurveyEdition arg = invocation.getArgument(0);
                        SurveyEmbeddableDto surveyDto = new SurveyEmbeddableDto(arg.getSurvey().getId().value(), arg.getSurvey().getTitle(), arg.getSurvey().getDescription(), null);
                        return new SurveyEditionResponseDto(arg.getId().value(), arg.getStartDate(), arg.getEndDate(), arg.getYear(), surveyDto);
                    });

            SurveyEditionResponseDto actual = sut.update(new SurveyEditionId(5L), requestDto);

            assertThat(actual).isNotNull();
            assertThat(actual.year()).isEqualTo(requestDto.year());
            verify(repository).findById(any(SurveyEditionId.class));
            verify(surveyRepository).findById(any(SurveyId.class));
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void given_surveyEditionDoesNotExist_whenDelete_shouldThrowEntityNotFoundException() {
            given(repository.existsById(any(SurveyEditionId.class))).willReturn(false);

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.delete(new SurveyEditionId(2L)))
                    .withMessageContaining("survey edition with id 2 not found");
        }

        @Test
        void given_surveyEditionExists_whenDelete_shouldDeleteSurveyEdition() {
            given(repository.existsById(any(SurveyEditionId.class))).willReturn(true);

            sut.delete(new SurveyEditionId(3L));

            verify(repository).existsById(any(SurveyEditionId.class));
            verify(repository).deleteById(any(SurveyEditionId.class));
        }
    }


}