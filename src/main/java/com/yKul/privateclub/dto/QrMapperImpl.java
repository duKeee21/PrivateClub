package com.yKul.privateclub.dto;

import com.yKul.privateclub.entity.QrCode;
import org.springframework.stereotype.Component;

@Component
public class QrMapperImpl implements QrMapper{
    @Override
    public QrDto toDto(QrCode qr) {
        if (qr == null) {
            return null;
        }

        return new QrDto(qr.getId(), qr.getUuid());
    }

}
