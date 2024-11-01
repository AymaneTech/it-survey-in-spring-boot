package com.wora.state_of_dev.owner.domain;

import com.wora.state_of_dev.common.domain.valueObject.Timestamp;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Table(name = "owners")

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Owner {

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
