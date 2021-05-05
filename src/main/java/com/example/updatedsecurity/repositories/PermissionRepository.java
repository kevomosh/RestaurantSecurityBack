package com.example.updatedsecurity.repositories;

import com.example.updatedsecurity.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsPermissionByCode(String code);
    Optional<Permission> findPermissionByCode(String code);
}
