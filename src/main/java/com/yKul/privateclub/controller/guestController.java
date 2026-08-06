package com.yKul.privateclub.controller;

import com.yKul.privateclub.service.guestService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class guestController {
 private final guestService guestService;

    public guestController(guestService guestService) {
        this.guestService = guestService;
    }
}
