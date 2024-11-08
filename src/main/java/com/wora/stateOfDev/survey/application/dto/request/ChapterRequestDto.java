package com.wora.stateOfDev.survey.application.dto.request;

import com.wora.stateOfDev.common.application.validation.ReferenceExists;
import com.wora.stateOfDev.survey.domain.entity.Chapter;
import com.wora.stateOfDev.survey.domain.entity.SurveyEdition;
import com.wora.stateOfDev.survey.domain.valueObject.ChapterId;
import com.wora.stateOfDev.survey.domain.valueObject.SurveyEditionId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChapterRequestDto(@NotBlank String title,
                                @NotNull @ReferenceExists(entityClass = SurveyEdition.class, idClass = SurveyEditionId.class, message = "no survey edition with this id")
                                Long surveyEditionId,
                                @ReferenceExists(entityClass = Chapter.class, idClass = ChapterId.class, message = "no parent chapter with this id")
                                Long parentChapterId
) {
}
