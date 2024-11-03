package com.wora.state_of_dev.survey.domain.exception;

public class GivenAnswerNotBelongToQuestion extends RuntimeException {
    public GivenAnswerNotBelongToQuestion(String message) {
        super(message);
    }
}
