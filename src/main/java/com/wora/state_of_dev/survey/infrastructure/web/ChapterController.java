package com.wora.state_of_dev.survey.infrastructure.web;

import com.wora.state_of_dev.survey.application.dto.request.ChapterRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.ChapterResponseDto;
import com.wora.state_of_dev.survey.application.service.ChapterService;
import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/survey-editions/{id}/chapters")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService service;

    @PostMapping
    public ResponseEntity<ChapterResponseDto> create(@PathVariable Long id, @RequestBody @Valid ChapterRequestDto dto) {
        ChapterResponseDto chapter = service.create(new SurveyEditionId(id), dto);
        return ResponseEntity.ok(chapter);
    }
}
