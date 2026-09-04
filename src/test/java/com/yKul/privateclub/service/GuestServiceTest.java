package com.yKul.privateclub.service;

import com.yKul.privateclub.dto.GuestCreateDto;
import com.yKul.privateclub.dto.GuestDto;
import com.yKul.privateclub.dto.GuestMapper;
import com.yKul.privateclub.dto.GuestUpdateDto;
import com.yKul.privateclub.entity.Guest;
import com.yKul.privateclub.entity.QrCode;
import com.yKul.privateclub.repository.GuestRepository;
import com.yKul.privateclub.repository.QrRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private GuestMapper guestMapper;

    @Mock
    private QrRepository qrRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaUpdate<Guest> criteriaUpdate;

    @Mock
    private Query query;

    @Mock
    private Root<Guest> root;

    @Mock
    private Path<Object> path;

    @Captor
    private ArgumentCaptor<Path<Object>> pathCaptor;

    @Captor
    private ArgumentCaptor<Object> valueCaptor;

    @InjectMocks
    private GuestService guestService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(guestService, "entityManager", entityManager);
    }

    @Test
    void allGuests_WhenEmpty_ReturnsEmptyList() {
        when(guestRepository.findAllWithQrCodes()).thenReturn(Collections.emptyList());

        List<GuestDto> result = guestService.allGuests();

        assertThat(result).isEmpty();
        verify(guestRepository).findAllWithQrCodes();
        verify(guestMapper, never()).toDto(any());
    }

    @Test
    void allGuests_WhenGuestsExist_ReturnsMappedDtos() {
        Guest guest1 = Guest.builder().id(1L).firstName("Kung").secondName("Lao").email("shlyapa@mail.com").build();
        Guest guest2 = Guest.builder().id(2L).firstName("Liu").secondName("Kang").email("drakon@mail.com").build();
        GuestDto dto1 = new GuestDto(1L, "Kung", "Lao", "shlyapa@mail.com", null);
        GuestDto dto2 = new GuestDto(2L, "Liu", "Kang", "drakon@mail.com", null);

        when(guestRepository.findAllWithQrCodes()).thenReturn(List.of(guest1, guest2));
        when(guestMapper.toDto(guest1)).thenReturn(dto1);
        when(guestMapper.toDto(guest2)).thenReturn(dto2);

        List<GuestDto> result = guestService.allGuests();

        assertThat(result).hasSize(2).containsExactly(dto1, dto2);
        verify(guestRepository).findAllWithQrCodes();
    }

    @Test
    void findById_Success() {
        Guest guest = Guest.builder().id(1L).firstName("Kung").secondName("Lao").email("shlyapa@mail.com").build();
        GuestDto expectedDto = new GuestDto(1L, "Kung", "Lao", "shlyapa@mail.com", null);

        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(guestMapper.toDto(guest)).thenReturn(expectedDto);

        GuestDto result = guestService.findById(1L);

        assertThat(result).isEqualTo(expectedDto);
        verify(guestRepository).findById(1L);
    }

    @Test
    void findById_NotFound_ThrowsException() {
        when(guestRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> guestService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void createGuest_Success() {
        GuestCreateDto inputDto = new GuestCreateDto("Shao", "Kahn", "molotok@mail.com");
        Guest initialGuest = Guest.builder().firstName("Shao").secondName("Kahn").email("molotok@mail.com").build();
        Guest savedGuest = Guest.builder().id(1L).firstName("Shao").secondName("Kahn").email("molotok@mail.com").build();
        GuestDto expectedDto = new GuestDto(1L, "Shao", "Kahn", "molotok@mail.com", UUID.randomUUID());

        when(guestMapper.toEntity(inputDto)).thenReturn(initialGuest);
        when(guestRepository.save(initialGuest)).thenReturn(savedGuest);
        when(qrRepository.save(any(QrCode.class))).thenAnswer(i -> i.getArgument(0));
        when(guestMapper.toDto(savedGuest)).thenReturn(expectedDto);

        GuestDto result = guestService.createGuest(inputDto);

        assertThat(result).isEqualTo(expectedDto);
        verify(qrRepository, times(1)).save(any(QrCode.class));
    }

    @Test
    void createGuest_WhenSaveFails_ThrowsException() {
        GuestCreateDto inputDto = new GuestCreateDto("Shao", "Kahn", "molotok@mail.com");
        Guest initialGuest = Guest.builder().firstName("Shao").secondName("Kahn").email("molotok@mail.com").build();

        when(guestMapper.toEntity(inputDto)).thenReturn(initialGuest);
        when(guestRepository.save(initialGuest)).thenThrow(new RuntimeException("Ошибка БД"));

        assertThatThrownBy(() -> guestService.createGuest(inputDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ошибка БД");
    }

    @Test
    void createGuest_DuplicateEmail_ThrowsException() {
        GuestCreateDto inputDto = new GuestCreateDto("Shao", "Kahn", "existing@mail.com");
        Guest initialGuest = Guest.builder().firstName("Shao").secondName("Kahn").email("existing@mail.com").build();

        when(guestMapper.toEntity(inputDto)).thenReturn(initialGuest);
        when(guestRepository.save(initialGuest))
                .thenThrow(new RuntimeException("Duplicate email"));

        assertThatThrownBy(() -> guestService.createGuest(inputDto))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void deleteGuest_Success() {
        Guest guest = Guest.builder().id(1L).isDeleted(false).build();
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));

        guestService.deleteGuest(1L);

        assertThat(guest.getIsDeleted()).isTrue();
        verify(guestRepository).save(guest);
    }

    @Test
    void deleteGuest_NotFound_ThrowsException() {
        when(guestRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> guestService.deleteGuest(404L))
                .isInstanceOf(EntityNotFoundException.class);
        verify(guestRepository, never()).save(any());
    }

    @Test
    void deleteGuest_WithQrCode_MarksQrCodeDeleted() {
        QrCode qrCode = QrCode.builder()
                .id(1L)
                .isDeleted(false)
                .build();
        Guest guest = Guest.builder()
                .id(1L)
                .isDeleted(false)
                .qrCodes(List.of(qrCode))
                .build();

        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));

        guestService.deleteGuest(1L);

        assertThat(qrCode.isDeleted()).isTrue();
        verify(guestRepository).save(guest);
    }

    private void mockCriteriaUpdate() {
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createCriteriaUpdate(Guest.class)).thenReturn(criteriaUpdate);
        when(criteriaUpdate.from(Guest.class)).thenReturn(root);
        doReturn(path).when(root).get(anyString());
        when(entityManager.createQuery(criteriaUpdate)).thenReturn(query);
    }

    @Test
    void updateGuest_NotFound_ThrowsException() {
        mockCriteriaUpdate();
        when(query.executeUpdate()).thenReturn(0);

        GuestUpdateDto dto = new GuestUpdateDto("NewName", null, null);

        assertThatThrownBy(() -> guestService.updateGuest(999L, dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void updateGuest_UpdatesFirstNameOnly() {
        mockCriteriaUpdate();
        when(query.executeUpdate()).thenReturn(1);

        GuestUpdateDto dto = new GuestUpdateDto("NewName", null, null);
        guestService.updateGuest(1L, dto);

        verify(criteriaUpdate, times(1)).set(pathCaptor.capture(), valueCaptor.capture());
        verify(query).executeUpdate();
    }

    @Test
    void updateGuest_UpdatesAllFields() {
        mockCriteriaUpdate();
        when(query.executeUpdate()).thenReturn(1);

        GuestUpdateDto dto = new GuestUpdateDto("NewName", "NewSecondname", "new@mail.com");
        guestService.updateGuest(1L, dto);

        verify(criteriaUpdate, times(3)).set(pathCaptor.capture(), valueCaptor.capture());
        verify(query).executeUpdate();
    }

    @Test
    void updateGuest_UpdatesEmailOnly() {
        mockCriteriaUpdate();
        when(query.executeUpdate()).thenReturn(1);

        GuestUpdateDto dto = new GuestUpdateDto(null, null, "newemail@mail.com");
        guestService.updateGuest(1L, dto);

        verify(criteriaUpdate, times(1)).set(pathCaptor.capture(), valueCaptor.capture());
        verify(query).executeUpdate();
    }

    @Test
    void updateGuest_SkipsNullAndBlankFields() {
        mockCriteriaUpdate();
        when(query.executeUpdate()).thenReturn(1);

        GuestUpdateDto dto = new GuestUpdateDto("", "  ", null);
        guestService.updateGuest(1L, dto);

        verify(criteriaUpdate, never()).set(pathCaptor.capture(), valueCaptor.capture());
        verify(query).executeUpdate();
    }

    @Test
    void updateGuest_NoChanges_ExecutesUpdate() {
        mockCriteriaUpdate();
        when(query.executeUpdate()).thenReturn(1);

        GuestUpdateDto dto = new GuestUpdateDto(null, null, null);
        guestService.updateGuest(1L, dto);

        verify(criteriaUpdate, never()).set(pathCaptor.capture(), valueCaptor.capture());
        verify(query).executeUpdate();
    }
}