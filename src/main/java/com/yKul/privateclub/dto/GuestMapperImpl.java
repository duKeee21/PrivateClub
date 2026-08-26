package com.yKul.privateclub.dto;

import com.yKul.privateclub.entity.Guest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GuestMapperImpl implements GuestMapper {

    @Override
    public GuestDto toDto(Guest guest) {
        if (guest==null) {
            return null;
        }

        UUID qr = (guest.getQrCodes() != null && !guest.getQrCodes().isEmpty())
                ? guest.getQrCodes().getFirst().getUuid()
                : null;

        return new GuestDto(
                guest.getId(),
                guest.getFirstName(),
                guest.getSecondName(),
                guest.getEmail(),
                qr
        );
    }

    public  Guest toEntity(GuestDto dto) {
        if (dto == null) {
            return null;
        }
        return Guest.builder()
                .id(dto.id())
                .firstName(dto.firstName())
                .secondName(dto.secondName())
                .email(dto.email())
                .build();
    }
}
