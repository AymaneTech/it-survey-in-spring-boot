package com.wora.state_of_dev.survey.application.service;

import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.owner.application.dto.OwnerEmbeddableDto;
import com.wora.state_of_dev.owner.domain.Owner;
import com.wora.state_of_dev.owner.domain.OwnerId;
import com.wora.state_of_dev.owner.domain.OwnerRepository;
import com.wora.state_of_dev.survey.application.dto.request.SurveyRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SurveyResponseDto;
import com.wora.state_of_dev.survey.application.mapper.SurveyMapper;
import com.wora.state_of_dev.survey.application.service.impl.DefaultSurveyService;
import com.wora.state_of_dev.survey.domain.entity.Survey;
import com.wora.state_of_dev.survey.domain.repository.SurveyRepository;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@DisplayName("Default survey service test")
@ExtendWith(MockitoExtension.class)
class DefaultSurveyServiceTest {
    @Mock
    private SurveyRepository repository;
    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private SurveyMapper mapper;
    @InjectMocks
    private DefaultSurveyService sut;

    private Owner owner;
    private Survey survey;

    @BeforeEach
    void setup() {
        owner = new Owner("aymane").setId(new OwnerId(1L));
        survey = new Survey("state of motherfuckers", "state of motherfuckers in morocco statistics")
                .setId(new SurveyId(1L))
                .setOwner(owner);
    }

    @Nested
    class FindAll {
        @Test
        void given_surveysExists_whenFindAll_shouldReturnSurveysList() {
            Survey survey1 = new Survey("state of dev", "state of dev in morocco")
                    .setId(new SurveyId(2L))
                    .setOwner(owner);
            List<Survey> expected = List.of(survey, survey1);

            given(repository.findAll()).willReturn(expected);
            given(mapper.toResponseDto(any(Survey.class)))
                    .willAnswer(invocation -> {
                        Survey arg = invocation.getArgument(0);
                        OwnerEmbeddableDto ownerResponse = new OwnerEmbeddableDto(owner.getId().value(), owner.getName());
                        return new SurveyResponseDto(arg.getId().value(), arg.getTitle(), arg.getDescription(), ownerResponse);
                    });

            List<SurveyResponseDto> actual = sut.findAll();

            assertThat(actual.size()).isEqualTo(2);
            assertThat(actual).isNotNull();
            verify(repository).findAll();
            verify(mapper, times(2)).toResponseDto(any(Survey.class));
        }

        @Test
        void given_surveysDoesNotExists_whenFindAll_shouldReturnEmptyList() {
            given(repository.findAll()).willReturn(List.of());

            List<SurveyResponseDto> actual = sut.findAll();

            assertThat(actual.isEmpty()).isTrue();
            verify(repository).findAll();
            verify(mapper, never()).toResponseDto(any(Survey.class));
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        void given_surveyIdExists_whenFindById_shouldReturnSurvey() {
            given(repository.findById(any(SurveyId.class))).willReturn(Optional.of(survey));
            given(mapper.toResponseDto(any(Survey.class)))
                    .willAnswer(invocation -> {
                        Survey arg = invocation.getArgument(0);
                        OwnerEmbeddableDto ownerResponse = new OwnerEmbeddableDto(owner.getId().value(), owner.getName());
                        return new SurveyResponseDto(arg.getId().value(), arg.getTitle(), arg.getDescription(), ownerResponse);
                    });

            SurveyResponseDto actual = sut.findById(new SurveyId(1L));

            assertThat(actual.title()).isEqualTo(survey.getTitle());
            assertThat(actual.description()).isEqualTo(survey.getDescription());
        }

        @Test
        void given_surveyDoesNotExist_whenFindById_shouldThrowEntityNotFoundException() {
            given(repository.findById(any(SurveyId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.findById(new SurveyId(2L)))
                    .withMessageContaining("survey with id 2 not found");
        }
    }

    @Nested
    class CreateTests {
        @Test
        void given_surveyRequestDto_whenCreate_shouldCreateAndReturnSurvey() {
            SurveyRequestDto expected = new SurveyRequestDto("state of motherfuckers", "state of motherfuckers in morocco", 1L);

            given(ownerRepository.findById(eq(owner.getId()))).willReturn(Optional.ofNullable(owner));
            given(mapper.toEntity(eq(expected))).willReturn(survey);
            given(repository.save(eq(survey))).willReturn(survey);
            given(mapper.toResponseDto(any(Survey.class)))
                    .willAnswer(invocation -> {
                        Survey arg = invocation.getArgument(0);
                        OwnerEmbeddableDto ownerResponse = new OwnerEmbeddableDto(owner.getId().value(), owner.getName());
                        return new SurveyResponseDto(arg.getId().value(), arg.getTitle(), arg.getDescription(), ownerResponse);
                    });

            SurveyResponseDto actual = sut.create(expected);

            assertThat(actual.title()).isEqualTo(expected.title());
            assertThat(actual).isNotNull();
            verify(ownerRepository).findById(any(OwnerId.class));
            verify(repository).save(any(Survey.class));
        }

        @Test
        void given_ownerIdDoesNotExists_whenCreate_shouldCreateAndReturnSurvey() {
            SurveyRequestDto expected = new SurveyRequestDto("state of motherfuckers", "state of motherfuckers in morocco", 1L);

            given(ownerRepository.findById(eq(owner.getId()))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.create(expected))
                    .withMessageContaining("owner with id 1 not found");
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void given_surveyDoesNotExists_whenUpdate_shouldThrowEntityNotFoundException() {
            SurveyRequestDto expected = new SurveyRequestDto("state of motherfuckers", "state of motherfuckers in morocco", 1L);

            given(repository.findById(new SurveyId(4L))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.update(new SurveyId(4L), expected))
                    .withMessageContaining("survey with id 4 not found");
        }

        @Test
        void given_ownerDoesNotExists_whenUpdate_shouldThrowEntityNotFoundException() {
            SurveyRequestDto expected = new SurveyRequestDto("state of motherfuckers", "state of motherfuckers in morocco", 1L);

            given(repository.findById(new SurveyId(1L))).willReturn(Optional.of(survey));
            given(ownerRepository.findById(any(OwnerId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.update(new SurveyId(1L), expected))
                    .withMessageContaining("owner with id 1 not found");
        }

        @Test
        void given_surveyRequestDto_whenUpdate_shouldUpdateAndReturnSurvey() {
            SurveyRequestDto expected = new SurveyRequestDto("updated title", "state of motherfuckers in morocco", 1L);

            given(repository.findById(new SurveyId(1L))).willReturn(Optional.of(survey));
            given(ownerRepository.findById(any(OwnerId.class))).willReturn(Optional.of(owner));
            given(mapper.toResponseDto(any(Survey.class)))
                    .willAnswer(invocation -> {
                        Survey arg = invocation.getArgument(0);
                        OwnerEmbeddableDto ownerResponse = new OwnerEmbeddableDto(owner.getId().value(), owner.getName());
                        return new SurveyResponseDto(arg.getId().value(), arg.getTitle(), arg.getDescription(), ownerResponse);
                    });

            SurveyResponseDto actual = sut.update(new SurveyId(1L), expected);

            assertThat(actual.title()).isEqualTo("updated title");
            assertThat(actual).isNotNull();
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void given_surveyDoesNotExists_whenDelete_shouldThrowEntityNotFoundException() {
            given(repository.existsById(any(SurveyId.class))).willReturn(false);

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.delete(new SurveyId(2L)))
                    .withMessageContaining("survey with id 2 not found");
        }

        @Test
        void given_surveyExists_whenDelete_shouldDeleteSurvey() {
            given(repository.existsById(any(SurveyId.class))).willReturn(true);

            sut.delete(new SurveyId(3L));

            verify(repository).existsById(any(SurveyId.class));
            verify(repository).deleteById(any(SurveyId.class));
        }
    }
}