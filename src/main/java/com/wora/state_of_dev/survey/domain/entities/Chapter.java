package com.wora.state_of_dev.survey.domain.entities;

import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "chapters")

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public  Chapter {

    private ChapterId id;
}
