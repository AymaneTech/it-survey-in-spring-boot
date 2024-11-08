package com.wora.stateOfDev.survey.domain.repository;

import com.wora.stateOfDev.survey.domain.entity.SurveyEdition;
import com.wora.stateOfDev.survey.domain.valueObject.SurveyEditionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyEditionRepository extends JpaRepository<SurveyEdition, SurveyEditionId> {
}
