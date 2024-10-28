package com.wora.state_of_dev.survey.infrastructure.web;

import com.wora.state_of_dev.survey.application.dto.request.SurveyEditionRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SurveyEditionResponseDto;
import com.wora.state_of_dev.survey.application.service.SurveyEditionService;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/survey-editions")
@RequiredArgsConstructor
public class SurveyEditionController {
    private final SurveyEditionService service;

    @GetMapping
    public ResponseEntity<List<SurveyEditionResponseDto>> findAll() {
        List<SurveyEditionResponseDto> surveyEditions = service.findAll();
        return ResponseEntity.ok(surveyEditions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyEditionResponseDto> findById(@PathVariable Long id) {
        SurveyEditionResponseDto surveyEdition = service.findById(new SurveyEditionId(id));
        return ResponseEntity.ok(surveyEdition);
    }

    @PostMapping
    public ResponseEntity<SurveyEditionResponseDto> create(@RequestBody @Valid SurveyEditionRequestDto dto) {
        SurveyEditionResponseDto surveyEdition = service.create(dto);
        return new ResponseEntity<>(surveyEdition, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyEditionResponseDto> update(@PathVariable Long id, @RequestBody @Valid SurveyEditionRequestDto dto) {
        SurveyEditionResponseDto surveyEdition = service.update(new SurveyEditionId(id), dto);
        return ResponseEntity.ok(surveyEdition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(new SurveyEditionId(id));
        return ResponseEntity.noContent().build();
    }
}
