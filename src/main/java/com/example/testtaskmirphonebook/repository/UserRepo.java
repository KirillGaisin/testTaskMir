package com.example.testtaskmirphonebook.repository;

import com.example.testtaskmirphonebook.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByName(String name);
}
