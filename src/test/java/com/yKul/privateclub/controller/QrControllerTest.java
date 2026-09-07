package com.yKul.privateclub.controller;

import com.yKul.privateclub.dto.QrDto;
import com.yKul.privateclub.service.QrCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class QrControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QrCodeService qrService;

    @Test
    void createQr_ShouldReturnCreatedStatusAndQrDto() throws Exception {
        Long guestId = 1L;
        QrDto responseDto = new QrDto(1L, UUID.randomUUID());
        when(qrService.createQr(guestId)).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/qr/guest/{guestId}/createqr", guestId))
                .andExpect(status().isCreated());

        verify(qrService).createQr(guestId);
    }

    @Test
    void recreateQr_ShouldReturnNewUuid() throws Exception {
        UUID oldUuid = UUID.randomUUID();
        UUID newUuid = UUID.randomUUID();
        when(qrService.recreateQr(oldUuid)).thenReturn(newUuid);

        mockMvc.perform(put("/api/v1/qr/{uuid}/newqr", oldUuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(newUuid.toString()));

        verify(qrService).recreateQr(oldUuid);
    }

    @Test
    void deleteQr_ShouldReturn204() throws Exception {
        UUID uuid = UUID.randomUUID();
        doNothing().when(qrService).deleteQr(uuid);

        mockMvc.perform(delete("/api/v1/qr/{uuid}", uuid))
                .andExpect(status().isNoContent());

        verify(qrService).deleteQr(uuid);
    }
}