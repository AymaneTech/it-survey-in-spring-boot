package com.wora.state_of_dev.user.domain.entity;

import com.wora.state_of_dev.user.domain.valueObject.AuthorityId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authorities")

@Getter
@Setter
@NoArgsConstructor
public class Authority implements Serializable {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private AuthorityId id;

    private String name;

    private String value;

    @ManyToMany
    private List<Role> roles = new ArrayList<>();

    public Authority(String name, String value) {
        this.name = name;
        this.value = value;
    }


}
