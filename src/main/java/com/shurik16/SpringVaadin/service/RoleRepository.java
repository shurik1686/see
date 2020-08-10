package com.shurik16.SpringVaadin.service;

import com.shurik16.SpringVaadin.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByAuthority(String roleName);
}