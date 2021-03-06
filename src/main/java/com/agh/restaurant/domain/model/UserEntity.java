/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agh.restaurant.domain.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity(name = "user")
@Table(name = "USER")
public class UserEntity extends AbstractEntity implements UserDetails, Serializable {

    private static final long serialVersionUID = 4815877135015943617L;

    @NotNull
    @Column(name = "USERNAME_", nullable = false, unique = true)
    private String username;

    @Column(name = "DISPLAYNAME_")
    private String displayName;

    @NotNull
    @Column(name = "PASSWORD_", nullable = false)
    private String password;

    @Column(name = "EMAIL_", nullable = false)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<RoleEntity> authorities;

    @OneToMany(mappedBy="waiter")
    private Collection<OrderEntity> realizedOrdersWaiter;

    @OneToMany(mappedBy="chef")
    private Collection<OrderEntity> realizedOrdersChef;

    @OneToMany(mappedBy="bartender")
    private Collection<OrderEntity> realizedOrdersBartender;

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

    public UserEntity withUsername(String user) {
        this.setUsername(user);
        return this;
    }

    public UserEntity withEmail(String s) {
        this.setEmail(s);
        return this;
    }

    public UserEntity withPassword(String s) {
        this.setPassword(s);
        return this;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserEntity))
            return false;
        if (!super.equals(o))
            return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getPassword(), that.getPassword()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getAuthorities(), that.getAuthorities()) &&
                Objects.equals(realizedOrdersWaiter, that.realizedOrdersWaiter) &&
                Objects.equals(realizedOrdersChef, that.realizedOrdersChef) &&
                Objects.equals(realizedOrdersBartender, that.realizedOrdersBartender);
    }

    @Override public int hashCode() {
        return Objects
                .hash(super.hashCode(), getUsername(), getDisplayName(), getPassword(), getEmail(), getAuthorities(),
                        realizedOrdersWaiter, realizedOrdersChef, realizedOrdersBartender);
    }
}
