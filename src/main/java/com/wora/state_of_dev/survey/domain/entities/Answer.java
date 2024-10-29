package com.wora.state_of_dev.survey.domain.entities;

import com.wora.state_of_dev.survey.domain.valueObject.AnswerId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "answers")

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Answer {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private AnswerId id;

    private String text;

    @Column(name = "select_count")
    private Integer selectCount;

    @ManyToOne
    private Question question;
}
