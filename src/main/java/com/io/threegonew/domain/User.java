package com.io.threegonew.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name="users")
@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    @Id
    @Column(name = "u_id", updatable = false, unique = true)
    private String id;

    @Column(name = "u_pw")
    private String pw;

    @Column(name = "u_name")
    private String name;

    @Column(name = "u_email", nullable = false, unique = true)
    private String email;

    @Column(name = "u_ofile")
    private String u_ofile;

    @Column(name = "u_sfile")
    private String u_sfile;

    @Column(name = "u_about")
    private String u_about;

    @Builder
    public User(String id, String pw, String name, String email, String u_ofile, String u_sfile, String u_about){
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.email = email;
        this.u_ofile = u_ofile;
        this.u_sfile = u_sfile;
        this.u_about = u_about;
    }

    //권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getPassword() {
        return pw;
    }

    @Override
    public String getUsername() {
        return id;
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
