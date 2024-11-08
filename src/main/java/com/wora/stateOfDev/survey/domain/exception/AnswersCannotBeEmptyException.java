package com.wora.stateOfDev.survey.domain.exception;

public class AnswersCannotBeEmptyException extends RuntimeException {
    public AnswersCannotBeEmptyException(String msg) {
        super(msg);
    }
}
