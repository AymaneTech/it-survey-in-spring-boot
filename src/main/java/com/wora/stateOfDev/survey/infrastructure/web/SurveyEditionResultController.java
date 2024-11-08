package com.wora.stateOfDev.survey.infrastructure.web;

import com.wora.stateOfDev.survey.application.dto.response.SurveyEditionResultResponseDto;
import com.wora.stateOfDev.survey.application.service.SurveyEditionResultService;
import com.wora.stateOfDev.survey.domain.valueObject.SurveyEditionId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/survey-editions/{id}/result")
@RequiredArgsConstructor
public class SurveyEditionResultController {

    private final SurveyEditionResultService service;

    @GetMapping
    public ResponseEntity<SurveyEditionResultResponseDto> getResult(@PathVariable Long id) {
        SurveyEditionResultResponseDto result = service.getResult(new SurveyEditionId(id));
        return ResponseEntity.ok(result);
    }
}
