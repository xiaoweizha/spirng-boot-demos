package com.example.demo.tools.service;

import java.io.OutputStream;

public interface QRCodeHandlerService {
    void encoderQRCode(String content, OutputStream outputStream);
}
