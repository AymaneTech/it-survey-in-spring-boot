package com.wora.stateOfDev.survey.infrastructure.web;

import com.wora.stateOfDev.survey.application.dto.request.QuestionRequestDto;
import com.wora.stateOfDev.survey.application.dto.response.QuestionResponseDto;
import com.wora.stateOfDev.survey.application.service.QuestionService;
import com.wora.stateOfDev.survey.domain.valueObject.QuestionId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
class QuestionController {
    private final QuestionService service;

    @GetMapping
    public ResponseEntity<List<QuestionResponseDto>> findAll() {
        List<QuestionResponseDto> questions = service.findAll();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> findById(@PathVariable Long id) {
        QuestionResponseDto question = service.findById(new QuestionId(id));
        return ResponseEntity.ok(question);
    }

    @PostMapping
    public ResponseEntity<QuestionResponseDto> create(@RequestBody @Valid QuestionRequestDto dto) {
        QuestionResponseDto question = service.create(dto);
        return new ResponseEntity<>(question, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> update(@PathVariable Long id, @RequestBody @Valid QuestionRequestDto dto) {
        QuestionResponseDto question = service.update(new QuestionId(id), dto);
        return ResponseEntity.ok(question);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(new QuestionId(id));
        return ResponseEntity.noContent().build();
    }
}
