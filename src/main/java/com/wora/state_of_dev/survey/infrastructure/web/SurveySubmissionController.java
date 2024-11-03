package com.wora.state_of_dev.survey.infrastructure.web;

import com.wora.state_of_dev.survey.application.dto.request.ListOfQuestionSubmissionRequestDto;
import com.wora.state_of_dev.survey.application.dto.request.SingleQuestionSubmissionRequestDto;
import com.wora.state_of_dev.survey.application.dto.request.SurveySubmission;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/survey-editions/{id}/participate")
public class SurveySubmissionController {

    @PostMapping
    public ResponseEntity<Object> participate(@PathVariable Long id, @RequestBody @Valid SurveySubmission dto) {
        if (dto instanceof SingleQuestionSubmissionRequestDto singleQuestion) {
            System.out.println("single one ");
            System.out.println(singleQuestion);
        } else if (dto instanceof ListOfQuestionSubmissionRequestDto listQuestion) {
            System.out.println("list of questions");
            System.out.println(listQuestion);
        }
        System.out.println("here here");
        return ResponseEntity.ok(dto);
    }
}
