package com.yKul.privateclub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record GuestCreateDto(
        @NotBlank (message = "Имя обязательно для заполнения")
        String firstName,

        @NotBlank (message = "Фамилия обязательна для заполнения")
        String secondName,

        @NotBlank (message = "Email обязательно для заполнения")
        @Email (message = "Некорректный email")
        String email
) {}