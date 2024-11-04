package com.wora.state_of_dev.survey.application.dto.request.submission;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wora.state_of_dev.survey.infrastructure.deserializer.QuestionSubmissionDeserializer;

@JsonDeserialize(using = QuestionSubmissionDeserializer.class)
public sealed interface SurveySubmission permits ListOfQuestionSubmissionRequestDto, SingleQuestionSubmissionRequestDto{
}
