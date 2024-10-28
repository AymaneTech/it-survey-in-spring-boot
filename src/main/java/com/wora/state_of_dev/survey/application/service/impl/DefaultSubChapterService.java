package com.wora.state_of_dev.survey.application.service.impl;

import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.survey.application.dto.request.ChapterRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SubChapterResponseDto;
import com.wora.state_of_dev.survey.application.mapper.ChapterMapper;
import com.wora.state_of_dev.survey.application.service.SubChapterService;
import com.wora.state_of_dev.survey.domain.entities.Chapter;
import com.wora.state_of_dev.survey.domain.repository.ChapterRepository;
import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class DefaultSubChapterService implements SubChapterService {
    private final ChapterRepository repository;
    private final ChapterMapper mapper;

    @Override
    public SubChapterResponseDto create(ChapterId parentId, ChapterRequestDto dto) {
        Chapter parentChapter = repository.findById(parentId)
                .orElseThrow(() -> new EntityNotFoundException("parent chapter", parentId.value()));

        Chapter chapter = new Chapter(dto.title())
                .setParentChapter(parentChapter);

        Chapter savedChapter = repository.save(chapter);
        return mapper.toSubChapterResponse(savedChapter);
    }
}
