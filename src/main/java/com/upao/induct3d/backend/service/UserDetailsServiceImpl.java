package com.upao.induct3d.backend.service;

import com.upao.induct3d.backend.entity.User;
import com.upao.induct3d.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!user.isActive()) {
            throw new UsernameNotFoundException("Usuario inactivo: " + username);
        }

        return UserPrincipal.build(user);
    }
}
