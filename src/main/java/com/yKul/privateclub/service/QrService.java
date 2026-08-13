package com.yKul.privateclub.service;

import com.yKul.privateclub.entity.Guest;
import com.yKul.privateclub.entity.QrCode;
import com.yKul.privateclub.repository.QrRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class QrService {

    private final QrRepository qrRepository;
    private final QrCodeGenerator qrCodeGenerator;
    private final GuestService guestService;

    public QrCode createQrCodeForGuest(Guest guest) {
        UUID qrForGuest = qrCodeGenerator.generateQr();

        QrCode qrCode = QrCode.builder()
                .uuid(qrForGuest)
                .guest(guest)
                .build();

        return qrRepository.save(qrCode);
    }


    public QrCode reGenerateQrCode(Long guestId) {
        qrRepository.deleteByGuestId(guestId);

        Guest guest = guestService.findGuestById(guestId);

        return createQrCodeForGuest(guest);
    }
}
