package com.yKul.privateclub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GuestUpdateDto(
        @NotBlank
        String firstName,

        @NotBlank
        String secondName,

        @NotBlank
        @Email
        String email
) {}