package com.wora.state_of_dev.survey.infrastructure.web;

import com.wora.state_of_dev.survey.application.dto.request.ChapterRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SubChapterResponseDto;
import com.wora.state_of_dev.survey.application.service.SubChapterService;
import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chapters/{chapterId}/sub-chapters")
@RequiredArgsConstructor
class SubChapterController {
    private final SubChapterService service;

    @PostMapping
    public ResponseEntity<SubChapterResponseDto> create(@PathVariable Long chapterId, @RequestBody @Valid ChapterRequestDto dto) {
        SubChapterResponseDto subChapter = service.create(new ChapterId(chapterId), dto);
        return new ResponseEntity<>(subChapter, HttpStatus.CREATED);
    }
}
