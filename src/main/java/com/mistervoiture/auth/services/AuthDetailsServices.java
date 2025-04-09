package com.mistervoiture.auth.services;

import com.mistervoiture.auth.repository.AuthRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthDetailsServices implements UserDetailsService {

    private final AuthRepository authRepository;

    public AuthDetailsServices(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return authRepository.findAuthByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username or Password Incorrect"));
    }
}