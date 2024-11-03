package com.wora.state_of_dev.survey.application.service;

import com.wora.state_of_dev.survey.application.dto.request.ListOfQuestionSubmissionRequestDto;
import com.wora.state_of_dev.survey.application.dto.request.SingleQuestionSubmissionRequestDto;

public interface SurveySubmissionService {
    void submit(SingleQuestionSubmissionRequestDto dto);

    void submit(ListOfQuestionSubmissionRequestDto dto);
}