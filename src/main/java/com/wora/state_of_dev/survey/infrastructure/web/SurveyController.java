package com.wora.state_of_dev.survey.infrastructure.web;

import com.wora.state_of_dev.survey.application.dto.request.SurveyRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SurveyResponseDto;
import com.wora.state_of_dev.survey.application.service.SurveyService;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/surveys")
@RequiredArgsConstructor
class SurveyController {
    private final SurveyService service;

    @GetMapping
    public ResponseEntity<List<SurveyResponseDto>> findAll() {
        List<SurveyResponseDto> surveys = service.findAll();
        return ResponseEntity.ok(surveys);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyResponseDto> findById(@PathVariable Long id) {
        SurveyResponseDto survey = service.findById(new SurveyId(id));
        return ResponseEntity.ok(survey);
    }

    @PostMapping
    public ResponseEntity<SurveyResponseDto> create(@RequestBody @Valid SurveyRequestDto dto) {
        SurveyResponseDto survey = service.create(dto);
        return new ResponseEntity<>(survey, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyResponseDto> update(@PathVariable Long id, @RequestBody @Valid SurveyRequestDto dto) {
        SurveyResponseDto survey = service.update(new SurveyId(id), dto);
        return ResponseEntity.ok(survey);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(new SurveyId(id));
        return ResponseEntity.noContent().build();
    }
}
