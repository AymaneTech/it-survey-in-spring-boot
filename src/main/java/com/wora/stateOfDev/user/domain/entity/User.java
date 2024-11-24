package com.wora.stateOfDev.user.domain.entity;

import com.wora.stateOfDev.user.domain.valueObject.Name;
import com.wora.stateOfDev.user.domain.valueObject.UserId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"first_name", "last_name"})
})

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class User implements UserDetails {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private UserId id;

    @Embedded
    private Name name;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    private String password;

    @ManyToOne
    private Role role;

    public User(String firstName, String lastName, String email, String password, Role role) {
        this.name = new Name(firstName, lastName);
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null || role.getAuthorities() == null) {
            return Collections.emptyList();
        }
        final List<SimpleGrantedAuthority> authorities = getGrantedAuthorities();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        return authorities;
    }

    private List<SimpleGrantedAuthority> getGrantedAuthorities() {
        return role.getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getValue()))
                .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
