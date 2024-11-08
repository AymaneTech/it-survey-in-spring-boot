package com.wora.state_of_dev.user.domain.entity;

import com.wora.state_of_dev.user.domain.valueObject.Name;
import com.wora.state_of_dev.user.domain.valueObject.UserId;
import jakarta.persistence.*;
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
@Table(name = "users")

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

    private String email;

    private String password;

    @ManyToOne
    private Role role;

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
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
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
