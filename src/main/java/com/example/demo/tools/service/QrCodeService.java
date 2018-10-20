package com.example.demo.tools.service;

import java.awt.image.BufferedImage;

public interface QrCodeService {

    BufferedImage createQrCode(String content);
}
