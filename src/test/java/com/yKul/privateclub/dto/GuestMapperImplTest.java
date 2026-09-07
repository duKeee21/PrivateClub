package com.yKul.privateclub.dto;

import com.yKul.privateclub.entity.Guest;
import com.yKul.privateclub.entity.QrCode;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GuestMapperImplTest {

    private final GuestMapper guestMapper = new GuestMapperImpl();

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
    void toDto_MultipleQrs_ReturnsFirstActiveQrUuid() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        QrCode qr1 = QrCode.builder().uuid(uuid1).isDeleted(false).build();
        QrCode qr2 = QrCode.builder().uuid(uuid2).isDeleted(false).build();

        Guest guest = Guest.builder()
                .id(1L)
                .firstName("Test")
                .qrCodes(List.of(qr1, qr2))
                .build();

        GuestDto result = guestMapper.toDto(guest);

        assertThat(result.qr()).isEqualTo(uuid1);
    }

    @Test
    void toDto_WithDeletedQr_SkipsDeletedAndReturnsActiveQrUuid() {
        UUID activeUuid = UUID.randomUUID();

        QrCode deletedQr = QrCode.builder().uuid(UUID.randomUUID()).isDeleted(true).build();
        QrCode activeQr = QrCode.builder().uuid(activeUuid).isDeleted(false).build();

        Guest guest = Guest.builder()
                .id(1L)
                .qrCodes(List.of(deletedQr, activeQr))
                .build();

        GuestDto result = guestMapper.toDto(guest);

        assertThat(result.qr()).isEqualTo(activeUuid);
    }

    @Test
    void toEntity_Null_ReturnsNull() {
        assertThat(guestMapper.toEntity(null)).isNull();
    }

    @Test
    void toEntity_ValidCreateDto_ReturnsGuestEntity() {
        GuestCreateDto createDto = new GuestCreateDto("Kung", "Lao", "shlyapa@mail.com");

        Guest entity = guestMapper.toEntity(createDto);

        assertThat(entity.getFirstName()).isEqualTo("Kung");
        assertThat(entity.getSecondName()).isEqualTo("Lao");
        assertThat(entity.getEmail()).isEqualTo("shlyapa@mail.com");
        assertThat(entity.getId()).isNull();
    }
}