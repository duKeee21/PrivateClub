package com.yKul.privateclub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yKul.privateclub.dto.GuestCreateDto;
import com.yKul.privateclub.dto.GuestUpdateDto;
import com.yKul.privateclub.entity.Guest;
import com.yKul.privateclub.repository.GuestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = "/sql/insert_guest.sql")
    void getOne_ShouldReturnGuest_WhenIdExists() throws Exception {
        Long guestId = 1L;

        mockMvc.perform(get("/api/v1/guests/{id}", guestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(guestId))
                .andExpect(jsonPath("$.firstName").value("Liu"))
                .andExpect(jsonPath("$.secondName").value("Kang"))
                .andExpect(jsonPath("$.email").value("drakon@mail.com"));
    }

    @Test
    void create_ShouldSaveGuestToDatabase() throws Exception {
        GuestCreateDto createDto = new GuestCreateDto("Sub", "Zero", "ice@mail.com");

        mockMvc.perform(post("/api/v1/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Sub"))
                .andExpect(jsonPath("$.email").value("ice@mail.com"));

        Guest savedGuest = guestRepository.findAll().stream()
                .filter(g -> "ice@mail.com".equals(g.getEmail()))
                .findFirst()
                .orElseThrow();

        assertThat(savedGuest.getFirstName()).isEqualTo("Sub");
        assertThat(savedGuest.getSecondName()).isEqualTo("Zero");
    }

    @Test
    @Sql(scripts = "/sql/insert_guest.sql")
    void update_ShouldUpdateGuestInDatabase() throws Exception {
        Long guestId = 1L;
        GuestUpdateDto updateDto = new GuestUpdateDto("Billy", "Butcher", "boy@mail.com");

        mockMvc.perform(put("/api/v1/guests/{id}", guestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());

        Guest updatedGuest = guestRepository.findById(guestId).orElseThrow();
        assertThat(updatedGuest.getFirstName()).isEqualTo("Billy");
        assertThat(updatedGuest.getSecondName()).isEqualTo("Butcher");
        assertThat(updatedGuest.getEmail()).isEqualTo("boy@mail.com");
    }

    @Test
    @Sql(scripts = "/sql/insert_guest.sql")
    void delete_ShouldMarkGuestAsDeletedInDatabase() throws Exception {
        Long guestId = 1L;

        mockMvc.perform(delete("/api/v1/guests/{id}", guestId))
                .andExpect(status().isNoContent());

        Guest deletedGuest = guestRepository.findById(guestId).orElseThrow();
        assertThat(deletedGuest.getIsDeleted()).isTrue();
    }

    @Test
    void create_ShouldReturnBadRequest_WhenFirstNameIsBlank() throws Exception {

        GuestCreateDto invalidDto = new GuestCreateDto("   ", "Lao", "shlyapa@mail.com");

        mockMvc.perform(post("/api/v1/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("firstName: Имя обязательно для заполнения"));
    }

    @Test
    void create_ShouldReturnBadRequest_WhenEmailIsInvalid() throws Exception {

        GuestCreateDto invalidDto = new GuestCreateDto("Kung", "Lao", "invalidMail");

        mockMvc.perform(post("/api/v1/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email: Некорректный email"));
    }

    @Test
    void getOne_ShouldReturnNotFound_WhenIdDoesNotExist() throws Exception {
        Long nonexistentId = 999L;

        mockMvc.perform(get("/api/v1/guests/{id}", nonexistentId))
                .andExpect(status().isNotFound());
    }
}