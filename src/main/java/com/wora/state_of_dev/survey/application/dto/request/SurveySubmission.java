package com.wora.state_of_dev.survey.application.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wora.state_of_dev.survey.application.mapper.QuestionSubmissionDeserializer;

@JsonDeserialize(using = QuestionSubmissionDeserializer.class)
public interface SurveySubmission {
}
