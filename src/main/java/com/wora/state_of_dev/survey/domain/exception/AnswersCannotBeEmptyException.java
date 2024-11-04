package com.wora.state_of_dev.survey.domain.exception;

public class AnswersCannotBeEmptyException extends RuntimeException {
    public AnswersCannotBeEmptyException(String msg) {
        super(msg);
    }
}
