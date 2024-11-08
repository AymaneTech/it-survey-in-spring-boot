package com.wora.stateOfDev.survey.domain.repository;

import com.wora.stateOfDev.survey.domain.entity.Survey;
import com.wora.stateOfDev.survey.domain.valueObject.SurveyId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, SurveyId> {
    boolean existsByTitle(String title);
}
