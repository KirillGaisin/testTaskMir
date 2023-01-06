package com.example.testtaskmirphonebook.repository;

import com.example.testtaskmirphonebook.entity.PhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PhoneRepo extends JpaRepository<PhoneEntity, UUID> {
    Optional<PhoneEntity> deleteByUserId(UUID userId);
}
