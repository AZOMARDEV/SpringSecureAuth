package com.mistervoiture.auth.entity;

import com.mistervoiture.auth.enums.BearerID;
import com.mistervoiture.auth.enums.CredentialStatus;
import com.mistervoiture.auth.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Auth implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "_id", unique = true, nullable = false, length = 36)
    private String _id;
    @Enumerated(EnumType.STRING)
    private CredentialStatus credential;
    private String bearer;
    private String fullname;
    private String username;
    private String pwd;
    @Enumerated(EnumType.STRING)
    private BearerID bearerId;
    private String token;
    private String mailBackup;
    private String secretNickname;
    private String codeNickname;
    @Enumerated(EnumType.STRING)
    private Roles role;
    private boolean isProfessionalAccount;
    private boolean status;
    private boolean isActive;
    private String languageAuth;
    private boolean isPhone;

    private boolean AccountNonExpired;
    private boolean AccountNonLocked;
    private boolean CredentialsNonExpired;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.pwd;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.AccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.AccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.CredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.status;
    }
}
