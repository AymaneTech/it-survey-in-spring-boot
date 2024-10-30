package com.wora.state_of_dev.survey.domain.entities;

import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

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
    @JoinColumn(name = "survey_edition_id")
    private SurveyEdition surveyEdition;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_chapter_id")
    private Chapter parentChapter;

    @OneToMany(mappedBy = "chapter", fetch = FetchType.EAGER)
    private List<Question> questions;

    public Chapter(String title) {
        this.title = title;
    }

    public boolean isSurveyEditionParent() {
        return surveyEdition != null && parentChapter == null;
    }

    public boolean isSubChapter() {
        return parentChapter != null && surveyEdition == null;
    }
}
