package com.shurik16.SpringVaadin.model;

import com.google.common.base.MoreObjects;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;


    private String username;

    private String password;
    //@Size(min=3)
    private String fullName;

    private boolean enabled = true;
    @Column(name = "accountnonlocked")
    private boolean accountNonLocked = true;
    @Column(name = "accountnonexpired")
    private boolean accountNonExpired = true;
    @Column(name = "credentialsnonexpired")
    private boolean credentialsNonExpired = true;

    @ManyToMany
    private List<Role> roles;

    public User() {
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            //foreign key for EmployeeEntity in employee_car table
            joinColumns = @JoinColumn(name = "user_id"),
            //foreign key for other side - EmployeeEntity in employee_car table
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Collection<Role> getAuthorities() {
        return roles;
    }

    public void setAuthorities(List<Role> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    @Transient
    public void setUnencryptedPassword(String password) {
        setPassword(new BCryptPasswordEncoder().encode(password));
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void addAuthority(Role role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .add("fullname", fullName)
                .add("roles", roles)
                .toString();
    }

    public boolean hasAuthority(String[] requiredRoles) {
        if (getAuthorities().isEmpty() && requiredRoles.length > 0) {
            return false;
        }

        for (String requiredRole : requiredRoles) {
            for (Role role : getAuthorities()) {
                if (role.getAuthority().equals(requiredRole)) {
                    return true;
                }
            }
        }
        return false;
    }
}