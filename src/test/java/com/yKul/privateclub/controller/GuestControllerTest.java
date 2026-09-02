package com.yKul.privateclub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yKul.privateclub.dto.GuestDto;
import com.yKul.privateclub.service.GuestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GuestService guestService;

    @Test
    void findAllGuests_ShouldReturnGuestList() throws Exception {
        GuestDto guest1 = new GuestDto(1L, "Kung", "Lao", "shlyapa2mail.com", UUID.randomUUID());
        GuestDto guest2 = new GuestDto(2L, "Shao", "Kang", "molotok@mail.com", UUID.randomUUID());
        when(guestService.allGuests()).thenReturn(List.of(guest1, guest2));

        mockMvc.perform(get("/api/v1/guests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(guestService).allGuests();
    }

    @Test
    void getOne_ShouldReturnGuest_WhenIdExists() throws Exception {
        Long guestId = 1L;
        GuestDto guestDto = new GuestDto(1L, "Liu", "Kang", "drakon@mail.com", UUID.randomUUID());
        when(guestService.findById(guestId)).thenReturn(guestDto);

        mockMvc.perform(get("/api/v1/guests/{id}", guestId))
                .andExpect(status().isOk());

        verify(guestService).findById(guestId);
    }

    @Test
    void create_ShouldReturnCreatedGuest() throws Exception {
        GuestDto inputDto = new GuestDto(1L, "Sonya", "Blade", "shpagat@mail.com", UUID.randomUUID());
        GuestDto createdDto = new GuestDto(2L, "Johnny", "Cage", "starr@mail.com", UUID.randomUUID());
        when(guestService.createGuest(any(GuestDto.class))).thenReturn(createdDto);

        mockMvc.perform(post("/api/v1/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());

        verify(guestService).createGuest(any(GuestDto.class));
    }

    @Test
    void delete_ShouldReturn204() throws Exception {
        Long guestId = 1L;
        doNothing().when(guestService).deleteGuest(guestId);

        mockMvc.perform(delete("/api/v1/guests/{id}", guestId))
                .andExpect(status().isNoContent());

        verify(guestService).deleteGuest(guestId);
    }

    @Test
    void update_ShouldReturn200() throws Exception {
        Long guestId = 1L;
        GuestDto inputDto = new GuestDto(1L, "Jax","Briggs","zhelezki@mail.com", UUID.randomUUID());
        doNothing().when(guestService).updateGuest(eq(guestId), any(GuestDto.class));

        mockMvc.perform(put("/api/v1/guests/{id}", guestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());

        verify(guestService).updateGuest(eq(guestId), any(GuestDto.class));
    }
}