package com.wora.state_of_dev.survey.infrastructure.web;

import com.wora.state_of_dev.survey.application.dto.request.ListOfQuestionSubmissionRequestDto;
import com.wora.state_of_dev.survey.application.dto.request.SingleQuestionSubmissionRequestDto;
import com.wora.state_of_dev.survey.application.dto.request.SurveySubmission;
import com.wora.state_of_dev.survey.application.service.SurveySubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/survey-editions/{id}/participate")
@RequiredArgsConstructor
public class SurveySubmissionController {
    private final SurveySubmissionService service;

    @PostMapping
    public ResponseEntity<Void> participate(@PathVariable Long id, @RequestBody @Valid SurveySubmission dto) {
        if (dto instanceof SingleQuestionSubmissionRequestDto singleSubmission) {
            service.submit(singleSubmission);
        } else if (dto instanceof ListOfQuestionSubmissionRequestDto listSubmissions) {
            service.submit(listSubmissions);
        }
        return ResponseEntity.noContent().build();
    }
}
