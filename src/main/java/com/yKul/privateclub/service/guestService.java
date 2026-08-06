package com.yKul.privateclub.service;

import com.yKul.privateclub.entity.guest;
import com.yKul.privateclub.repository.guestRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class guestService {
    private final guestRepository guestRepository;

    public guestService(guestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @GetMapping
    public List<guest> findAll() {
        return guestRepository.findAll();
    }
}
