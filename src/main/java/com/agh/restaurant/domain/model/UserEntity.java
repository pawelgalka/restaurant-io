package com.agh.restaurant.domain.model;

import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity(name = "user")
@Table(name = "USER")
public class UserEntity implements UserDetails {

    private static final long serialVersionUID = 4815877135015943617L;

    @Id()
    @Column(name = "USER_ID_")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "USERNAME_", nullable = false, unique = true)
    private String username;

    @Column(name = "DISPLAYNAME_")
    private String displayName;

    @Column(name = "PASSWORD_", nullable = false)
    private String password;

    @Column(name = "EMAIL_", nullable = false)
    @Email
    //TODO add unique index
    private String email;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<RoleEntity> authorities;

    @OneToMany(mappedBy="waiter", fetch = FetchType.LAZY)
    private Collection<FeedbackEntity> waiterFeedbacks;

    @OneToMany(mappedBy="bartender", fetch = FetchType.LAZY)
    private Collection<FeedbackEntity> bartenderFeedbacks;

    @OneToMany(mappedBy="chef", fetch = FetchType.LAZY)
    private Collection<FeedbackEntity> chefFeedbacks;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TableEntity> tableEntities;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAccountNonExpired() {
        return false;
    }

    public boolean isAccountNonLocked() {
        return false;
    }

    public boolean isCredentialsNonExpired() {
        return false;
    }

    public boolean isEnabled() {
        return false;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(List<RoleEntity> authorities) {
        this.authorities = authorities;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
