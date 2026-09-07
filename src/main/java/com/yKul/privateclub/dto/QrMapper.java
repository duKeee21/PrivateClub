package com.yKul.privateclub.dto;

import com.yKul.privateclub.entity.QrCode;

public interface QrMapper {

    QrDto toDto(QrCode qr);

}
