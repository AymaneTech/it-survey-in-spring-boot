package com.wora.stateOfDev.survey.domain.repository;

import com.wora.stateOfDev.survey.domain.entity.Answer;
import com.wora.stateOfDev.survey.domain.valueObject.AnswerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, AnswerId> {
}
