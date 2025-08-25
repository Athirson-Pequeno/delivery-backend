package com.tizo.delivery.model;

import com.tizo.delivery.model.enums.StoreUserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final StoreUser storeUser;

    public UserDetailsImpl(StoreUser storeUser) {
        this.storeUser = storeUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + storeUser.getRole().name());
    }

    @Override
    public String getPassword() {
        return storeUser.getPassword();
    }

    @Override
    public String getUsername() {
        return storeUser.getEmail();
    }

    public StoreUserRole getRole() {
        return storeUser.getRole();
    }
}
