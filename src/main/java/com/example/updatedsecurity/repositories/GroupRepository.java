package com.example.updatedsecurity.repositories;

import com.example.updatedsecurity.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findGroupByCode(String code);
}
