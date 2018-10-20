package com.example.demo.tools.service;

import java.io.OutputStream;

public interface QRCodeWithPictureService {
    void encode(String content, String imgPath, OutputStream output, boolean needCompress);
}
