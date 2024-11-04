package com.wora.state_of_dev.survey.application.service;

import com.wora.state_of_dev.survey.application.dto.request.submission.ListOfQuestionSubmissionRequestDto;
import com.wora.state_of_dev.survey.application.dto.request.submission.SingleQuestionSubmissionRequestDto;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;

public interface SurveySubmissionService {
    void submit(SurveyEditionId surveyEditionId, SingleQuestionSubmissionRequestDto dto);

    void submit(SurveyEditionId surveyEditionId, ListOfQuestionSubmissionRequestDto dto);
}