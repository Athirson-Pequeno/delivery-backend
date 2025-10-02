package com.tizo.delivery.service;

import com.tizo.delivery.model.store.StoreUser;
import com.tizo.delivery.model.auth.UserDetailsImpl;
import com.tizo.delivery.repository.StoreUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final StoreUserRepository userRepository;

    public UserDetailsServiceImpl(StoreUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        StoreUser storeUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDetailsImpl(storeUser);
    }
}

