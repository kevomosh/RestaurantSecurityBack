package com.example.updatedsecurity.repositories;

import com.example.updatedsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByName(String name);

    @Query(value = "select  u from User u left join fetch u.userGroups where u.name = :name")
    Optional<User> getUserByName(@Param("name") String name);
}
