package com.yKul.privateclub.repository;

import com.yKul.privateclub.entity.Guest;
import com.yKul.privateclub.entity.QrCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GuestRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GuestRepository guestRepository;

    @Test
    void findByEmail_Success() {
        Guest guest = Guest.builder()
                .firstName("Kung")
                .secondName("Lao")
                .email("shlyapa@mail.com")
                .build();
        entityManager.persistAndFlush(guest);

        Optional<Guest> found = guestRepository.findByEmail("shlyapa@mail.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("shlyapa@mail.com");
    }

    @Test
    void softDelete_IgnoredByQuery() {
        Guest guest = Guest.builder()
                .firstName("Liu")
                .secondName("Kang")
                .email("drakon@mail.com")
                .isDeleted(true)
                .build();
        entityManager.persistAndFlush(guest);

        Optional<Guest> found = guestRepository.findByEmail("drakon@mail.com");

        assertThat(found).isEmpty();
    }

    @Test
    void findAllWithQrCodes_Success() {
        Guest guest = Guest.builder()
                .firstName("Shao")
                .secondName("Kahn")
                .email("molotok@mail.com")
                .build();
        entityManager.persistAndFlush(guest);

        QrCode qr = QrCode.builder()
                .uuid(UUID.randomUUID())
                .guest(guest)
                .build();
        entityManager.persistAndFlush(qr);

        guest.addQr(qr);
        entityManager.merge(guest);
        entityManager.flush();

        entityManager.clear();

        List<Guest> result = guestRepository.findAllWithQrCodes();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getQrCodes()).hasSize(1);
        assertThat(result.getFirst().getQrCodes().getFirst().getUuid()).isEqualTo(qr.getUuid());
    }
}