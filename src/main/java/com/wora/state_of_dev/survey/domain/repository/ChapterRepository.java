package com.wora.state_of_dev.survey.domain.repository;

import com.wora.state_of_dev.survey.domain.entities.Chapter;
import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, ChapterId> {
    @Query("SELECT c FROM Chapter c WHERE c.surveyEdition.id = :id")
    List<Chapter> findAllBySurveyEditionId(SurveyEditionId id);

    boolean existsByTitleAndSurveyEditionId(@NotBlank String title, SurveyEditionId id);
}
