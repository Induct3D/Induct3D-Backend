package com.upao.induct3d.backend.service;

import com.upao.induct3d.backend.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private final String username;
    private final String email;
    private final String password;
    private final String name;
    private final String surname;

    public UserPrincipal(String username, String email, String password, String name, String surname) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public static UserPrincipal build(User user) {
        return new UserPrincipal(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getSurname()
        );
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override public String getPassword() {
        return password;
    }

    @Override public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override public boolean isAccountNonExpired() {
        return true;
    }

    @Override public boolean isAccountNonLocked() {
        return true;
    }

    @Override public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override public boolean isEnabled() {
        return true;
    }
}
