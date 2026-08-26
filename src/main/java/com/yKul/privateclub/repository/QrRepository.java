package com.yKul.privateclub.repository;

import com.yKul.privateclub.entity.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface QrRepository extends JpaRepository<QrCode, Long> {

    Optional<QrCode> findByUuid(UUID uuid);
}
