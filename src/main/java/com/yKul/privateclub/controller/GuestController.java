package com.yKul.privateclub.controller;

import com.yKul.privateclub.entity.Guest;
import com.yKul.privateclub.service.GuestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guests")
@AllArgsConstructor
public class GuestController {

    private final GuestService service;

    @GetMapping()
    public List<Guest> findAllGuests() {

        return service.allGuests();
    }

    @GetMapping("/{id}")
    public Guest getOne(@PathVariable Long id) {

        return service.findById(id);
    }

    @PostMapping
    public Guest create(@RequestBody Guest guest) {
        return service.createGuest(guest);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteGuest(id);
    }

    @PutMapping(path = "/{id}")
    public void update(@PathVariable Long id,
                            @RequestParam(required = false) String email,
                            @RequestParam(required = false) String firstName,
                            @RequestParam(required = false) String secondName) {
        service.updateGuest(id, firstName, secondName, email);
    }
}
