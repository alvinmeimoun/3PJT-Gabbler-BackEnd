package com.supinfo.gabbler.server.repository;

import com.supinfo.gabbler.server.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor {
}