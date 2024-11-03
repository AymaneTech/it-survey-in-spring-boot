package com.wora.state_of_dev.survey.domain.exception;

public class QuestionNotBelongToSurveyEdition extends RuntimeException {
    public QuestionNotBelongToSurveyEdition(String s) {
        super(s);
    }
}
