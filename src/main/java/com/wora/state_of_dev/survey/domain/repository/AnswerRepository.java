package com.wora.state_of_dev.survey.domain.repository;

import com.wora.state_of_dev.survey.domain.entity.Answer;
import com.wora.state_of_dev.survey.domain.valueObject.AnswerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, AnswerId> {
}
