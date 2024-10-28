package com.wora.state_of_dev.survey.domain.entities;

import com.wora.state_of_dev.common.domain.valueObject.Timestamp;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.Year;

@Entity
@Table(name = "survey_editions")

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class SurveyEdition {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private SurveyEditionId id;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    private Year year;

    @ManyToOne
    private Survey survey;

    private Timestamp timestamp;

    public SurveyEdition(LocalDateTime startDate, LocalDateTime endDate, Year year) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.year = year;
    }

}
