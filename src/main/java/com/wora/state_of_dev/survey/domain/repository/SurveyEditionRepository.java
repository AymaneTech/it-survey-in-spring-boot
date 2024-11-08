package com.wora.state_of_dev.survey.domain.repository;

import com.wora.state_of_dev.survey.domain.entity.SurveyEdition;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyEditionRepository extends JpaRepository<SurveyEdition, SurveyEditionId> {
}
