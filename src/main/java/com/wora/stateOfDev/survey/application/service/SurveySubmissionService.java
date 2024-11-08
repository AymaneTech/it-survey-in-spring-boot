package com.wora.stateOfDev.survey.application.service;

import com.wora.stateOfDev.survey.application.dto.request.submission.ListOfQuestionSubmissionRequestDto;
import com.wora.stateOfDev.survey.application.dto.request.submission.SingleQuestionSubmissionRequestDto;
import com.wora.stateOfDev.survey.domain.valueObject.SurveyEditionId;

public interface SurveySubmissionService {
    void submit(SurveyEditionId surveyEditionId, SingleQuestionSubmissionRequestDto dto);

    void submit(SurveyEditionId surveyEditionId, ListOfQuestionSubmissionRequestDto dto);
}