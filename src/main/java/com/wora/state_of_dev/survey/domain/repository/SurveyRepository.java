package com.wora.state_of_dev.survey.domain.repository;

import com.wora.state_of_dev.survey.domain.entity.Survey;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, SurveyId> {
    boolean existsByTitle(String title);
}
