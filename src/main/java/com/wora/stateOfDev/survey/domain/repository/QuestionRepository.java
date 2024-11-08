package com.wora.stateOfDev.survey.domain.repository;

import com.wora.stateOfDev.survey.domain.entity.Question;
import com.wora.stateOfDev.survey.domain.valueObject.QuestionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, QuestionId> {
}
