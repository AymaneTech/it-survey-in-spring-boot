package com.wora.state_of_dev.survey.domain.exception;

public class SurveyIsClosed extends RuntimeException {
    public SurveyIsClosed(String s) {
        super(s);
    }
}
