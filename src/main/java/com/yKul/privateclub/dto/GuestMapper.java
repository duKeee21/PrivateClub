package com.yKul.privateclub.dto;

import com.yKul.privateclub.entity.Guest;

public interface GuestMapper {

    GuestDto toDto(Guest g);

    Guest toEntity(GuestCreateDto dto);
}
