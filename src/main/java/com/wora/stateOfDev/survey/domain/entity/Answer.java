package com.wora.stateOfDev.survey.domain.entity;

import com.wora.stateOfDev.survey.domain.valueObject.AnswerId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Entity
@Table(name = "answers")

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Answer implements Serializable {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private AnswerId id;

    private String text;

    @Column(name = "select_count")
    private int selectCount;

    @ManyToOne(cascade = CascadeType.ALL)
    private Question question;

    public Answer(Long id, String text) {
        this.id = new AnswerId(id);
        this.text = text;
    }

    public void incrementSelectCount() {
        this.selectCount++;
    }
}
