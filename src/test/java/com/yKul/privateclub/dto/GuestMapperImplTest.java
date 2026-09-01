package com.yKul.privateclub.dto;

import com.yKul.privateclub.entity.Guest;
import com.yKul.privateclub.entity.QrCode;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GuestMapperImplTest {

    private final GuestMapperImpl guestMapper = new GuestMapperImpl();

    @Test
    void toDto_Null_ReturnsNull() {
        assertThat(guestMapper.toDto(null)).isNull();
    }

    @Test
    void toDto_EmptyQrList_ReturnsDtoWithNullQr() {
        Guest guest = Guest.builder().id(1L).qrCodes(Collections.emptyList()).build();

        GuestDto result = guestMapper.toDto(guest);

        assertThat(result.qr()).isNull();
    }

    @Test
    void toDto_MultipleQrs_ReturnsFirstQrUuid() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        QrCode qr1 = QrCode.builder().uuid(uuid1).build();
        QrCode qr2 = QrCode.builder().uuid(uuid2).build();

        Guest guest = Guest.builder()
                .id(1L)
                .firstName("Test")
                .qrCodes(List.of(qr1, qr2))
                .build();

        GuestDto result = guestMapper.toDto(guest);

        assertThat(result.qr()).isEqualTo(uuid1);
    }

    @Test
    void toEntity_Null_ReturnsNull() {
        assertThat(guestMapper.toEntity(null)).isNull();
    }

    @Test
    void toEntity_Valid_ReturnsGuestEntity() {
        GuestDto dto = new GuestDto(1L, "Kung", "Lao", "shlyapa@mail.com", UUID.randomUUID());

        Guest entity = guestMapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getFirstName()).isEqualTo("Kung");
        assertThat(entity.getSecondName()).isEqualTo("Lao");
        assertThat(entity.getEmail()).isEqualTo("shlyapa@mail.com");
    }
}