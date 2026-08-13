package com.yKul.privateclub.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QrCodeGenerator {

    public UUID generateQr() {
        return UUID.randomUUID();
    }
}