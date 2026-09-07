package com.yKul.privateclub.controller;

import com.yKul.privateclub.dto.GuestCreateDto;
import com.yKul.privateclub.dto.GuestDto;
import com.yKul.privateclub.dto.GuestUpdateDto;
import com.yKul.privateclub.service.GuestService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<GuestDto> create(@Valid @RequestBody GuestCreateDto guestCreateDto) {
        return ResponseEntity.ok(service.createGuest(guestCreateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteGuest(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuestDto> update(
            @PathVariable Long id,
            @Valid @RequestBody GuestUpdateDto guestUpdateDto) {
        service.updateGuest(id, guestUpdateDto);
        return ResponseEntity.ok().build();
    }
}