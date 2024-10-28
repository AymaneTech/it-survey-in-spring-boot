package com.wora.state_of_dev.survey.domain.entities;

import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Table(name = "chapters")

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
public class Chapter {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private ChapterId id;

    @NotBlank
    private String title;

    @ManyToOne
    private SurveyEdition surveyEdition;

    @ManyToOne(cascade = CascadeType.ALL)
    private Chapter subChapter;

    public Chapter(String title) {
        this.title = title;
    }
}
