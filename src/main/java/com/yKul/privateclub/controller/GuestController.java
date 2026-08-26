package com.yKul.privateclub.controller;

import com.yKul.privateclub.dto.GuestDto;
import com.yKul.privateclub.service.GuestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/guests")
@AllArgsConstructor
public class GuestController {

    private final GuestService service;

    @GetMapping
    public ResponseEntity<List<GuestDto>> findAllGuests() {
        return ResponseEntity.ok(service.allGuests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<GuestDto> create(@RequestBody GuestDto guestDto) {
        return ResponseEntity.ok(service.createGuest(guestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteGuest(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuestDto> update(
            @PathVariable Long id,
            @RequestBody GuestDto guestDto) {
        GuestDto updatedGuest = service.updateGuest(id, guestDto);
        return ResponseEntity.ok(updatedGuest);
    }

    @PutMapping("/qr/useqr")
    public ResponseEntity<UUID> rotateQr(@RequestParam UUID currentUuid) {
        UUID newUuid = service.newQr(currentUuid);
        return ResponseEntity.ok(newUuid);
    }
}