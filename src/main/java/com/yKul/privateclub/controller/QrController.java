package com.yKul.privateclub.controller;

import com.yKul.privateclub.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/qr")
@RequiredArgsConstructor

public class QrController {

    private final GuestService service;

    @PutMapping("/useqr")
    public ResponseEntity<UUID> rotateQr(@RequestParam UUID currentUuid) {
        UUID newUuid = service.newQr(currentUuid);
        return ResponseEntity.ok(newUuid);
    }
}
