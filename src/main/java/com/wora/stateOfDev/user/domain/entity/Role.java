package com.wora.stateOfDev.user.domain.entity;

import com.wora.stateOfDev.user.domain.valueObject.RoleId;
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

    @Column(unique = true)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "role_authorities",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private List<Authority> authorities = new ArrayList<>();

    public Role(String name, List<Authority> authorities) {
        this.name = name;
        this.authorities = authorities;
    }
}
