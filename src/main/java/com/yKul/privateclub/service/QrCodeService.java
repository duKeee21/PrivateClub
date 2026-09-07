package com.yKul.privateclub.service;

import com.yKul.privateclub.dto.QrDto;
import com.yKul.privateclub.dto.QrMapper;
import com.yKul.privateclub.entity.Guest;
import com.yKul.privateclub.entity.QrCode;
import com.yKul.privateclub.repository.GuestRepository;
import com.yKul.privateclub.repository.QrRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeService {

    private final QrRepository qrRepository;
    private final GuestRepository guestRepository;
    private final QrMapper qrMapper;

    @Transactional
    public QrDto createQr(Long guestId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Гость не найден с id: " + guestId));

        QrCode qrCode = QrCode.builder()
                .uuid(UUID.randomUUID())
                .guest(guest)
                .build();

        return qrMapper.toDto(qrRepository.save(qrCode));
    }

    @Transactional
    public UUID recreateQr(UUID currentUuid) {
        QrCode qrCode = qrRepository.findByUuid(currentUuid)
                .orElseThrow(() -> new EntityNotFoundException("QR-код не найден: " + currentUuid));
        UUID newUuid = UUID.randomUUID();
        qrCode.setUuid(newUuid);
        qrRepository.save(qrCode);

        return newUuid;
    }

    @Transactional
    public void deleteQr(UUID uuid) {
        QrCode qrCode = qrRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("QR-код не найден: " + uuid));

        qrCode.setDeleted(true);
        qrRepository.save(qrCode);
    }
}