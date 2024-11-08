package com.wora.state_of_dev.survey.domain.repository;

import com.wora.state_of_dev.survey.domain.entity.Question;
import com.wora.state_of_dev.survey.domain.valueObject.QuestionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, QuestionId> {
}
