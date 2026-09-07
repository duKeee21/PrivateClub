package com.yKul.privateclub.service;

import com.yKul.privateclub.dto.QrDto;
import com.yKul.privateclub.dto.QrMapper;
import com.yKul.privateclub.entity.Guest;
import com.yKul.privateclub.entity.QrCode;
import com.yKul.privateclub.repository.GuestRepository;
import com.yKul.privateclub.repository.QrRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QrCodeServiceTest {

    @Mock
    private QrRepository qrRepository;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private QrMapper qrMapper;

    @InjectMocks
    private QrCodeService qrCodeService;

    @Test
    void createQr_GuestNotFound_ThrowsException() {
        when(guestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> qrCodeService.createQr(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("1");
    }

    @Test
    void createQr_Success() {
        Guest guest = Guest.builder().id(1L).build();
        QrCode savedQr = QrCode.builder().id(10L).uuid(UUID.randomUUID()).guest(guest).build();
        QrDto dto = new QrDto(10L, savedQr.getUuid());

        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(qrRepository.save(any(QrCode.class))).thenReturn(savedQr);
        when(qrMapper.toDto(savedQr)).thenReturn(dto);

        QrDto result = qrCodeService.createQr(1L);

        assertThat(result).isEqualTo(dto);
        verify(qrRepository).save(any(QrCode.class));
    }

    @Test
    void recreateQr_NotFound_ThrowsException() {
        UUID nonExistent = UUID.randomUUID();
        when(qrRepository.findByUuid(nonExistent)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> qrCodeService.recreateQr(nonExistent))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void recreateQr_Success() {
        UUID oldUuid = UUID.randomUUID();
        QrCode qrCode = QrCode.builder().id(5L).uuid(oldUuid).build();

        when(qrRepository.findByUuid(oldUuid)).thenReturn(Optional.of(qrCode));

        UUID newUuid = qrCodeService.recreateQr(oldUuid);

        assertThat(newUuid).isNotNull().isNotEqualTo(oldUuid);
        assertThat(qrCode.getUuid()).isEqualTo(newUuid);
        verify(qrRepository).save(qrCode);
    }

    @Test
    void deleteQr_NotFound_ThrowsException() {
        UUID uuid = UUID.randomUUID();
        when(qrRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> qrCodeService.deleteQr(uuid))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteQr_Success() {
        UUID uuid = UUID.randomUUID();
        QrCode qrCode = QrCode.builder().id(1L).uuid(uuid).isDeleted(false).build();

        when(qrRepository.findByUuid(uuid)).thenReturn(Optional.of(qrCode));

        qrCodeService.deleteQr(uuid);

        assertThat(qrCode.isDeleted()).isTrue();
        verify(qrRepository).save(qrCode);
    }
}