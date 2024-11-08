package com.wora.stateOfDev.survey.domain.exception;

public class QuestionNotBelongToSurveyEdition extends RuntimeException {
    public QuestionNotBelongToSurveyEdition(String s) {
        super(s);
    }
}
