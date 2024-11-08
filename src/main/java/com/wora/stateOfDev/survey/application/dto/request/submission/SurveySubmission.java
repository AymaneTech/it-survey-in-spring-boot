package com.wora.stateOfDev.survey.application.dto.request.submission;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wora.stateOfDev.survey.infrastructure.deserializer.QuestionSubmissionDeserializer;

@JsonDeserialize(using = QuestionSubmissionDeserializer.class)
public sealed interface SurveySubmission permits ListOfQuestionSubmissionRequestDto, SingleQuestionSubmissionRequestDto{
}
