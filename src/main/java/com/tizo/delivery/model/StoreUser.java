package com.tizo.delivery.model;

import com.tizo.delivery.model.enums.StoreUserRole;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "store_users")
public class StoreUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private StoreUserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public StoreUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StoreUserRole getRole() {
        return role;
    }

    public void setRole(StoreUserRole role) {
        this.role = role;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StoreUser storeUser)) return false;
        return Objects.equals(id, storeUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
