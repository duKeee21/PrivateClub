package com.yKul.privateclub.repository;

import com.yKul.privateclub.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    Optional<Guest> findByEmail(String email);

    @Query("SELECT g FROM Guest g JOIN FETCH g.qrCodes")
    List<Guest> findAllWithQrCodes();
}
