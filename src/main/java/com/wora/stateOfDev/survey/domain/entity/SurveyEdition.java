package com.wora.stateOfDev.survey.domain.entity;

import com.wora.stateOfDev.common.domain.valueObject.Timestamp;
import com.wora.stateOfDev.survey.domain.valueObject.SurveyEditionId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey_editions")

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class SurveyEdition implements Serializable {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private SurveyEditionId id;

    @NotNull
//    @FutureOrPresent
    private LocalDateTime startDate;

    @NotNull
    @Future
    private LocalDateTime endDate;

    @NotNull
    @FutureOrPresent
    private Year year;

    @ManyToOne
    private Survey survey;

    @OneToMany(mappedBy = "surveyEdition", fetch = FetchType.EAGER)
    private List<Chapter> chapters = new ArrayList<>();

    private Timestamp timestamp;

    public SurveyEdition(LocalDateTime startDate, LocalDateTime endDate, Year year) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.year = year;
    }

}
