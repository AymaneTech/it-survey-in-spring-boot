package com.wora.state_of_dev.user.domain.entity;

import com.wora.state_of_dev.user.domain.valueObject.RoleId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")

@Getter
@Setter
@NoArgsConstructor
public class Role implements Serializable {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private RoleId id;

    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Authority> authorities = new ArrayList<>();

    public Role(String name, List<Authority> authorities) {
        this.name = name;
        this.authorities = authorities;
    }
}