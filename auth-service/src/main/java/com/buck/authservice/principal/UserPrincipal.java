package com.buck.authservice.principal;

import com.buck.authservice.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true; // hoặc logic tùy vào hệ thống
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // hoặc logic tùy vào hệ thống
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // hoặc logic tùy vào hệ thống
    }

    @Override
    public boolean isEnabled() {
        return true; // hoặc user.isEnabled() nếu có field tương ứng
    }

    public User getUser() {
        return user;
    }
}
