package com.yKul.privateclub.service;

import com.yKul.privateclub.dto.GuestDto;
import com.yKul.privateclub.dto.GuestMapper;
import com.yKul.privateclub.entity.Guest;
import com.yKul.privateclub.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;

    public List<GuestDto> allGuests() {
        return guestRepository.findAll()
                .stream()
                .map(GuestMapper::toDto)
                .collect(Collectors.toList());
    }

    public GuestDto findById(Long id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Гостя с id: " + id + " не существует!"));
        return GuestMapper.toDto(guest);
    }

    public GuestDto createGuest(GuestDto guestDto) {
        guestRepository.findByEmail(guestDto.email())
                .ifPresent(g -> {
                    throw new IllegalStateException("Гость с такой почтой уже зарегистрирован!");
                });


        Guest guest = GuestMapper.toEntity(guestDto);
        Guest s = guestRepository.save(guest);

        return GuestMapper.toDto(s);
    }

    public void deleteGuest(Long id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Гостя с id: " + id + " не существует!"));
        guestRepository.delete(guest);
    }

    public GuestDto updateGuest(Long id, GuestDto guestDto) {

        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Гостя с id: " + id + " не существует!"));

        if (guestDto.email() != null && !guestDto.email().equals(guest.getEmail())) {
            guestRepository.findByEmail(guestDto.email())
                    .ifPresent(g -> {
                        throw new IllegalStateException("Гость с такой почтой уже зарегистрирован!");
                    });
            guest.setEmail(guestDto.email());
        }

        if (guestDto.firstName() != null && !guestDto.firstName().equals(guest.getFirstName())) {
            guest.setFirstName(guestDto.firstName());
        }

        if (guestDto.secondName() != null && !guestDto.secondName().equals(guest.getSecondName())) {
            guest.setSecondName(guestDto.secondName());
        }

        Guest updatedGuest = guestRepository.save(guest);
        return GuestMapper.toDto(updatedGuest);

    }

    public Guest findGuestById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Гостя с id: " + id + " не существует!"));
    }
}
