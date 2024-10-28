package com.wora.state_of_dev.survey.application.service.impl;

import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.survey.application.dto.request.ChapterRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.ChapterResponseDto;
import com.wora.state_of_dev.survey.application.mapper.ChapterMapper;
import com.wora.state_of_dev.survey.application.service.ChapterService;
import com.wora.state_of_dev.survey.domain.entities.Chapter;
import com.wora.state_of_dev.survey.domain.entities.SurveyEdition;
import com.wora.state_of_dev.survey.domain.repository.ChapterRepository;
import com.wora.state_of_dev.survey.domain.repository.SurveyEditionRepository;
import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class DefaultChapterService implements ChapterService {
    private final ChapterRepository repository;
    private final SurveyEditionRepository surveyEditionRepository;
    private final ChapterMapper mapper;

//    @Override
//    public List<ChapterResponseDto> findAll() {
//        return repository.findAll()
//                .stream().map(mapper::toResponseDto)
//                .toList();
//    }
//
//    @Override
//    public ChapterResponseDto findById(ChapterId id) {
//        return repository.findById(id)
//                .map(mapper::toResponseDto)
//                .orElseThrow(() -> new EntityNotFoundException("chapter", id));
//    }

    @Override
    public ChapterResponseDto create(SurveyEditionId id, ChapterRequestDto dto) {
        SurveyEdition surveyEdition = surveyEditionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("survey edition", id));

        Chapter chapter = mapper.toEntity(dto)
                .setSurveyEdition(surveyEdition);
        Chapter savedChapter = repository.save(chapter);
        return mapper.toResponseDto(savedChapter);
    }
//    @Override
//    public ChapterResponseDto update(ChapterId id, ChapterRequestDto dto) {
//        Chapter chapter = repository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("chapter ", id));
//        SurveyEdition surveyEdition = surveyEditionRepository.findById(new SurveyEditionId(dto.surveyEditionId()))
//                .orElseThrow(() -> new EntityNotFoundException("survey edition", id));
//
//        chapter.setTitle(dto.title())
//                .setSurveyEdition(surveyEdition);
//        return mapper.toResponseDto(chapter);
//    }
//
//    @Override
//    public void delete(ChapterId id) {
//        if (!repository.existsById(id))
//            throw new EntityNotFoundException("chapter", id);
//        repository.deleteById(id);
//    }
}
