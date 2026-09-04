package com.yKul.privateclub.repository;

import com.yKul.privateclub.entity.Guest;
import com.yKul.privateclub.entity.QrCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QrRepositoryTest {

    private final TestEntityManager entityManager;
    private final QrRepository qrRepository;

    @Autowired
    QrRepositoryTest(TestEntityManager entityManager, QrRepository qrRepository) {
        this.entityManager = entityManager;
        this.qrRepository = qrRepository;
    }

    @Test
    void findByUuid_Success() {
        Guest guest = Guest.builder()
                .firstName("Kung")
                .secondName("Lao")
                .email("shlyapa@mail.com")
                .build();
        entityManager.persist(guest);

        UUID uuid = UUID.randomUUID();
        QrCode qrCode = QrCode.builder()
                .uuid(uuid)
                .guest(guest)
                .build();
        entityManager.persistAndFlush(qrCode);

        Optional<QrCode> found = qrRepository.findByUuid(uuid);

        assertThat(found).isPresent();
        assertThat(found.get().getUuid()).isEqualTo(uuid);
    }

    @Test
    void findByUuid_DeletedItem_ReturnsEmpty() {
        Guest guest = Guest.builder()
                .firstName("Liu")
                .secondName("Kang")
                .email("drakon@mail.com")
                .build();
        entityManager.persist(guest);

        UUID uuid = UUID.randomUUID();
        QrCode qrCode = QrCode.builder()
                .uuid(uuid)
                .isDeleted(true)
                .guest(guest)
                .build();
        entityManager.persistAndFlush(qrCode);

        Optional<QrCode> found = qrRepository.findByUuid(uuid);

        assertThat(found).isEmpty();
    }
}