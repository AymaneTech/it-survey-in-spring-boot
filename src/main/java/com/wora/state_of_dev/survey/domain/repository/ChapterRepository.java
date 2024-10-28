package com.wora.state_of_dev.survey.domain.repository;

import com.wora.state_of_dev.survey.domain.entities.Chapter;
import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter, ChapterId> {
}
