package com.wora.state_of_dev.survey.application.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.wora.state_of_dev.survey.application.dto.request.submission.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;

public class QuestionSubmissionDeserializer extends JsonDeserializer<SurveySubmission> {
    private static final String QUESTION_ID_FIELD = "questionId";
    private static final String SUBMISSIONS_FIELD = "submissions";
    private static final String ANSWERS_FIELD = "answer";

    @Override
    public SurveySubmission deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException {
        JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);
        try {
            if (rootNode.has(SUBMISSIONS_FIELD)) {
                return handleMultipleSubmissions(rootNode, jsonParser);
            } else if (rootNode.has(QUESTION_ID_FIELD)) {
                return handleSingleSubmission(rootNode);
            }
            throw new RuntimeException("json format is invalid");
        } catch (Exception e) {
            throw new JsonMappingException(jsonParser, "Failed to deserialize: " + e.getMessage(), e);
        }
    }

    private ListOfQuestionSubmissionRequestDto handleMultipleSubmissions(JsonNode rootNode, JsonParser jsonParser) throws JsonMappingException {
        JsonNode submissionsNode = rootNode.get(SUBMISSIONS_FIELD);
        validateSubmissionsArray(submissionsNode, jsonParser);

        List<SingleQuestionSubmissionRequestDto> submissions = StreamSupport.stream(submissionsNode.spliterator(), false)
                .map(this::mapToSingleQuestionSubmissionRequest)
                .toList();

        return new ListOfQuestionSubmissionRequestDto(submissions);
    }

    private SurveySubmission handleSingleSubmission(JsonNode node) {
        return mapToSingleQuestionSubmissionRequest(node);
    }

    private void validateSubmissionsArray(JsonNode submissionsNode, JsonParser jsonParser) throws JsonMappingException {
        if (!submissionsNode.isArray()) {
            throw new JsonMappingException(jsonParser, "Submissions field must be an array");
        }
    }

    private SingleQuestionSubmissionRequestDto mapToSingleQuestionSubmissionRequest(JsonNode node) {
        Long questionId = node.get(QUESTION_ID_FIELD).asLong();
        AnswerSubmissionRequestDto<?> answer = parseAnswer(node.get(ANSWERS_FIELD));
        return new SingleQuestionSubmissionRequestDto(questionId, answer);
    }

    private AnswerSubmissionRequestDto<?> parseAnswer(JsonNode answerNode) {
        if (answerNode == null) {
            throw new IllegalArgumentException("Answer node is missing or invalid");
        }

        return determineAnswerType(answerNode);
    }

    private AnswerSubmissionRequestDto<?> determineAnswerType(JsonNode answersNode) {
        if (answersNode.isArray()) {
            return new MultiChoiceSubmissionRequestDto(parseArrayAnswer(answersNode));
        } else if (answersNode.isNumber()) {
            return new SingleChoiceSubmissionRequestDto(answersNode.asLong());
        }
        throw new IllegalArgumentException("Unsupported answer type: expected array or number");
    }

    private List<Long> parseArrayAnswer(JsonNode arrayNode) {
        if (StreamSupport.stream(arrayNode.spliterator(), false).anyMatch(node -> !node.isNumber())) {
            throw new IllegalArgumentException("Array contains non-numeric values");
        }

        return StreamSupport.stream(arrayNode.spliterator(), false)
                .filter(JsonNode::isNumber)
                .map(JsonNode::asLong)
                .toList();
    }
}
