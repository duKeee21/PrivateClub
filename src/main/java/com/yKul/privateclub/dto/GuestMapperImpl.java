package com.yKul.privateclub.dto;

import com.yKul.privateclub.entity.Guest;
import com.yKul.privateclub.entity.QrCode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GuestMapperImpl implements GuestMapper {

    public Guest toEntity(GuestCreateDto dto) {

        if (dto == null) {
            return null;
        }

        return Guest.builder()
                .firstName(dto.firstName())
                .secondName(dto.secondName())
                .email(dto.email())
                .build();
    }

    public GuestDto toDto(Guest guest) {

        if (guest == null) {
            return null;
        }

        UUID qrCodeUuid = null;
        if (guest.getQrCodes() != null && !guest.getQrCodes().isEmpty()) {
            qrCodeUuid = guest.getQrCodes().stream()
                    .filter(qr -> !qr.isDeleted())
                    .map(QrCode::getUuid)
                    .findFirst()
                    .orElse(null);
        }

        return new GuestDto(
                guest.getId(),
                guest.getFirstName(),
                guest.getSecondName(),
                guest.getEmail(),
                qrCodeUuid
        );
    }
}