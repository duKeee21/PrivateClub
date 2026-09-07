package com.yKul.privateclub.dto;

import com.yKul.privateclub.entity.QrCode;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class QrMapperImplTest {

    private final QrMapperImpl qrMapper = new QrMapperImpl();

    @Test
    void toDto_Null_ReturnsNull() {
        assertThat(qrMapper.toDto(null)).isNull();
    }

    @Test
    void toDto_Valid_ReturnsDto() {
        UUID uuid = UUID.randomUUID();
        QrCode qr = QrCode.builder().id(10L).uuid(uuid).build();

        QrDto dto = qrMapper.toDto(qr);

        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.uuid()).isEqualTo(uuid);
    }
}