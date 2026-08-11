package com.yKul.privateclub.service;

import com.yKul.privateclub.entity.Guest;
import com.yKul.privateclub.repository.GuestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;

    public List<Guest> allGuests() {
        return guestRepository.findAll();
    }

    public Guest findById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Гостя с id: " + id + " не существует!"));
    }

    public Guest createGuest(Guest guest) {
        Optional<Guest> optionalGuest = guestRepository.findByEmail(guest.getEmail());
        if (optionalGuest.isPresent()) {
            throw new IllegalStateException("Гость с такой почтой уже зарегистрирован!");
        }
        return guestRepository.save(guest);
    }

    public void deleteGuest(Long id) {
        findById(id);
        guestRepository.deleteById(id);
    }

    public void updateGuest(Long id, String firstName, String secondName, String email) {

        Guest guest = findById(id);

        if (email != null && !email.equals(guest.getEmail())) {
            Optional<Guest> foundByEmailGuest = guestRepository.findByEmail(guest.getEmail());
            if (foundByEmailGuest.isPresent()) {
                throw new IllegalStateException("Гость с такой почтой уже зарегистрирован!");
            }
            guest.setEmail(email);
        }

        if (firstName != null && !firstName.equals(guest.getFirstName())) {
            guest.setFirstName(firstName);
        }

        if (secondName != null && !secondName.equals(guest.getSecondName())) {
            guest.setSecondName(secondName);
        }

        guestRepository.save(guest);


    }
}
