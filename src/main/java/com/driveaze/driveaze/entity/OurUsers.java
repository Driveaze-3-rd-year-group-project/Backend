package com.driveaze.driveaze.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "ourusers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OurUsers implements UserDetails {

    @Id
    @Column(name = "id", length =45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "contact_number", length = 100)
    private String contactNumber;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "role", length = 100)
    private String role;

    @Column(name = "registered_date", length =100, nullable = false)
    private LocalDate registeredDate;

    @Column(name = "is_verified_phone")
    private boolean isVerifiedPhone = false;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
