package com.wora.state_of_dev.survey.domain.entities;

import com.wora.state_of_dev.survey.domain.valueObject.AnswerType;
import com.wora.state_of_dev.survey.domain.valueObject.QuestionId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "questions")

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Question implements Serializable {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private QuestionId id;

    private String text;

    @Column(name = "answer_count")
    private int answerCount;

    @Enumerated(EnumType.STRING)
    private AnswerType answerType;

    @ManyToOne
    private Chapter chapter;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers;

    public Question(Long id, String text, AnswerType answerType, Chapter chapter) {
        this.id = new QuestionId(id);
        this.text = text;
        this.answerType = answerType;
        this.chapter = chapter;
    }

    public Question _setAnswers(List<Answer> answers) {
        answers.forEach(answer -> answer.setQuestion(this));
        this.answers = answers;
        return this;
    }

    public void incrementAnswerCount() {
        this.answerCount++;
    }
}
