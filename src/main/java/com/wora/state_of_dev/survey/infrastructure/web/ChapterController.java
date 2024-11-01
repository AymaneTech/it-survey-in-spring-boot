package com.wora.state_of_dev.survey.infrastructure.web;

import com.wora.state_of_dev.survey.application.dto.request.ChapterRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.ChapterResponseDto;
import com.wora.state_of_dev.survey.application.service.ChapterService;
import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class ChapterController {
    private final ChapterService service;

    @PostMapping("/survey-editions/{id}/chapters")
    public ResponseEntity<ChapterResponseDto> create(@PathVariable Long id, @RequestBody @Valid ChapterRequestDto dto) {
        ChapterResponseDto chapter = service.create(new SurveyEditionId(id), dto);
        return new ResponseEntity<>(chapter, HttpStatus.CREATED);
    }

    @GetMapping("/survey-editions/{id}/chapters")
    public ResponseEntity<List<ChapterResponseDto>> findAllBySurveyEditionId(@PathVariable Long id) {
        List<ChapterResponseDto> chapters = service.findAllBySurveyEditionId(new SurveyEditionId(id));
        return ResponseEntity.ok(chapters);
    }

    @GetMapping("/chapters/{id}")
    public ResponseEntity<ChapterResponseDto> findById(@PathVariable Long id) {
        ChapterResponseDto chapter = service.findById(new ChapterId(id));
        return ResponseEntity.ok(chapter);
    }

    @PutMapping("/chapters/{id}")
    public ResponseEntity<ChapterResponseDto> update(@PathVariable Long id, @RequestBody @Valid ChapterRequestDto dto) {
        ChapterResponseDto chapter = service.update(new ChapterId(id), dto);
        return ResponseEntity.ok(chapter);
    }

    @DeleteMapping("/chapters/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(new ChapterId(id));
        return ResponseEntity.noContent().build();
    }
}
