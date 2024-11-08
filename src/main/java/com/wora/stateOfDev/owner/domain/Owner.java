package com.wora.stateOfDev.owner.domain;

import com.wora.stateOfDev.common.domain.valueObject.Timestamp;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Entity
@Table(name = "owners")

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Owner implements Serializable {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private OwnerId id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    @Embedded
    private Timestamp timestamp;

    public Owner(String name) {
        this.name = name;
    }
}
