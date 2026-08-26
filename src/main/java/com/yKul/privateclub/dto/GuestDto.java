package com.yKul.privateclub.dto;


import java.util.UUID;

public record GuestDto(
        Long id,
        String firstName,
        String secondName,
        String email,
        UUID qr
) { }
