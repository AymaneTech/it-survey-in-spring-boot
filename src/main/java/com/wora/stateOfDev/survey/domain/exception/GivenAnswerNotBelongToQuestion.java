package com.wora.stateOfDev.survey.domain.exception;

public class GivenAnswerNotBelongToQuestion extends RuntimeException {
    public GivenAnswerNotBelongToQuestion(String message) {
        super(message);
    }
}
