package com.wora.state_of_dev.survey.domain.entities;

import com.wora.state_of_dev.owner.domain.Owner;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Entity
@Table(name = "surveys")

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Survey implements Serializable {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private SurveyId id;

    @Column(unique = true)
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    private Owner owner;

    public Survey(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
