package com.yKul.privateclub.service;

import com.yKul.privateclub.dto.GuestDto;
import com.yKul.privateclub.dto.GuestMapper;
import com.yKul.privateclub.entity.Guest;
import com.yKul.privateclub.entity.QrCode;
import com.yKul.privateclub.repository.GuestRepository;
import com.yKul.privateclub.repository.QrRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;
    private final QrRepository qrRepository;
    @PersistenceContext
    private EntityManager entityManager;

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

    @Transactional
    public GuestDto createGuest(GuestDto guestDto) {

        Guest guest = guestMapper.toEntity(guestDto);
        Guest savedGuest = guestRepository.save(guest);

        QrCode qrCode = QrCode.builder()
                .uuid(UUID.randomUUID())
                .guest(guest)
                .build();
        guest.addQr(qrCode);

        qrRepository.save(qrCode);
        savedGuest.addQr(qrCode);

        return guestMapper.toDto(savedGuest);
    }

    @Transactional
    public void deleteGuest(Long id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Гостя с id: " + id + " не существует!"));

        guest.setIsDeleted(true);

        if (guest.getQrCodes() != null) {
            guest.getQrCodes().forEach(qr -> {
                qr.setDeleted(true);
                qrRepository.save(qr);
            });
        }

        guestRepository.save(guest);
    }

    @Transactional
    public void updateGuest(Long id, GuestDto guestDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Guest> update = cb.createCriteriaUpdate(Guest.class);
        Root<Guest> root = update.from(Guest.class);

        if (guestDto.firstName() != null && !guestDto.firstName().isBlank()) {
            update.set(root.get("firstName"), guestDto.firstName());
        }

        if (guestDto.secondName() != null && !guestDto.secondName().isBlank()) {
            update.set(root.get("secondName"), guestDto.secondName());
        }

        if (guestDto.email() != null && !guestDto.email().isBlank()) {
            update.set(root.get("email"), guestDto.email());
        }

        update.where(cb.equal(root.get("id"), id));
        int updatedCount = entityManager.createQuery(update).executeUpdate();

        if (updatedCount == 0) {
            throw new EntityNotFoundException("Гостя с id: " + id + " не существует!");
        }
    }

}