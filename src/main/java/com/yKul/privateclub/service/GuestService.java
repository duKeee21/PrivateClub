package com.yKul.privateclub.service;

import com.yKul.privateclub.dto.GuestDto;
import com.yKul.privateclub.dto.GuestMapper;
import com.yKul.privateclub.entity.Guest;
import com.yKul.privateclub.entity.QrCode;
import com.yKul.privateclub.repository.GuestRepository;
import com.yKul.privateclub.repository.QrRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestService {
    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;
    private final QrRepository qrRepository;

    @Transactional(readOnly = true)
    public List<GuestDto> allGuests() {
        return guestRepository.findAllWithQrCodes()
                .stream()
                .map(guestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GuestDto findById(Long id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Гостя с id: " + id + " не существует!"));
        return guestMapper.toDto(guest);
    }

    public GuestDto createGuest(GuestDto guestDto) {

        Guest guest = guestMapper.toEntity(guestDto);

        QrCode qrCode = QrCode.builder()
                .uuid(UUID.randomUUID())
                .build();
        guest.addQr(qrCode);

        Guest savedGuest = guestRepository.save(guest);

        return guestMapper.toDto(savedGuest);
    }

    public void deleteGuest(Long id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Гостя с id: " + id + " не существует!"));

        guest.setIsDeleted(true);
    }

    public GuestDto updateGuest(Long id, GuestDto guestDto) {

        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Гостя с id: " + id + " не существует!"));

        if (guestDto.firstName() != null && !guestDto.firstName().equals(guest.getFirstName())) {
            guest.setFirstName(guestDto.firstName());
        }

        if (guestDto.secondName() != null && !guestDto.secondName().equals(guest.getSecondName())) {
            guest.setSecondName(guestDto.secondName());
        }

        return guestMapper.toDto(guest);
    }

    public UUID newQr(UUID currentUuid) {
        QrCode qrCode = qrRepository.findByUuid(currentUuid)
                .orElseThrow(() -> new EntityNotFoundException("QR" + currentUuid + " не найден!"));

        UUID newUuid = UUID.randomUUID();
        qrCode.setUuid(newUuid);
        qrRepository.save(qrCode);

        return newUuid;
    }
}
