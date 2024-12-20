package com.wora.stateOfDev.common.infrastructure.web;

import com.wora.stateOfDev.common.domain.ErrorResponse;
import com.wora.stateOfDev.common.domain.exception.EntityCreationException;
import com.wora.stateOfDev.common.domain.exception.EntityNotFoundException;
import com.wora.stateOfDev.survey.domain.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String VALIDATION_FAILED_MESSAGE = "Validation failed";
    public static final String ENTITY_NOT_FOUND_MESSAGE = "Resource Not Found";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(final MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            final String fieldName = ((FieldError) error).getField();
            final String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                VALIDATION_FAILED_MESSAGE,
                request.getDescription(false),
                errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse dataIntegrityViolation(DataIntegrityViolationException e, WebRequest request) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "Data Integrity Violation",
                request.getDescription(false),
                e.getMostSpecificCause().getMessage()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotFoundException(final EntityNotFoundException ex, WebRequest request) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                ENTITY_NOT_FOUND_MESSAGE,
                request.getDescription(false),
                ex.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalArgumentException(final IllegalArgumentException ex, WebRequest request) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "illegal argument exception",
                request.getDescription(false),
                ex.getMessage()
        );
    }

    @ExceptionHandler(EntityCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse entityCreationException(final EntityCreationException ex, WebRequest request) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                String.join("\n", ex.errors())
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse runtime(RuntimeException e, WebRequest request) {
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                "this exception not specified, caught just by global exception",
                request.getDescription(false),
                e.getMessage()
        );
    }

    @ExceptionHandler(ChapterHasSubChaptersException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse chapterHasSubChapters(ChapterHasSubChaptersException e, WebRequest request) {
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                "conflict in question information",
                request.getDescription(false),
                e.getMessage()
        );
    }

    @ExceptionHandler(GivenAnswerNotBelongToQuestion.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse givenAnswerNotBelongToQuestion(GivenAnswerNotBelongToQuestion e, WebRequest request) {
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                "the given answer is a not related to the given question",
                request.getDescription(false),
                e.getMessage()
        );
    }

    @ExceptionHandler(QuestionNotBelongToSurveyEdition.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse questionNotBelongToSurveyEdition(QuestionNotBelongToSurveyEdition e, WebRequest request) {
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                "given question not belong to the given survey edition",
                request.getDescription(false),
                e.getMessage()
        );
    }

    @ExceptionHandler(SurveyEditionNotOpenedNow.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse surveyEditionNotOpenedNow(SurveyEditionNotOpenedNow e, WebRequest request) {
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                "survey edition not opened now",
                request.getDescription(false),
                e.getMessage()
        );
    }

    @ExceptionHandler(AnswersCannotBeEmptyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse answerCannotBeEmpty(AnswersCannotBeEmptyException e, WebRequest request) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "cannot create question without its answers",
                request.getDescription(false),
                e.getMessage()
        );
    }

}
