package com.yKul.privateclub.dto;

import com.yKul.privateclub.entity.Guest;

public class GuestMapper {

    public static GuestDto toDto(Guest g) {
        return new GuestDto(
                g.getId(),
                g.getFirstName(),
                g.getSecondName(),
                g.getEmail()
        );
    }

    public static Guest toEntity(GuestDto dto) {
        Guest g = new Guest();
        g.setFirstName(dto.firstName());
        g.setSecondName(dto.secondName());
        g.setEmail(dto.email());
        return g;
    }
}
