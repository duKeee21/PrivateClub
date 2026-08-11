package com.yKul.privateclub.repository;

import com.yKul.privateclub.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    @Query(value = "SELECT * FROM guests WHERE email = :email", nativeQuery = true)
    Optional<Guest> findByEmail(String email);
}
