package com.yKul.privateclub.controller;

import com.yKul.privateclub.dto.QrDto;
import com.yKul.privateclub.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/qr")
@RequiredArgsConstructor
public class QrController {

    private final QrCodeService qrService;

    @PostMapping("/guest/{guestId}/createqr")
    public ResponseEntity<QrDto> createQr(@PathVariable Long guestId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(qrService.createQr(guestId));
    }

    @PutMapping("/{uuid}/newqr")
    public ResponseEntity<UUID> recreateQr(@PathVariable UUID uuid) {
        return ResponseEntity.ok(qrService.recreateQr(uuid));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteQr(@PathVariable UUID uuid) {
        qrService.deleteQr(uuid);
        return ResponseEntity.noContent().build();
    }
}