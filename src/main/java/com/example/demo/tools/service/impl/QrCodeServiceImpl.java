package com.example.demo.tools.service.impl;

import com.example.demo.common.util.MatrixToImageWriter;
import com.example.demo.tools.service.QrCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Service
public class QrCodeServiceImpl implements QrCodeService {
    Logger logger = LoggerFactory.getLogger(QrCodeServiceImpl.class);

    private BufferedImage createQrCode(String content, int width, int height) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");//内容所使用字符集编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);//纠错级别（L 7%、M 15%、Q 25%、H 30%）
        hints.put(EncodeHintType.MARGIN, 1);//设置二维码边的空度，非负整数
        BufferedImage qrcode = null;
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height,hints);
            qrcode = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            logger.error("createQrCode error.", e);
        }
        return qrcode;
    }

    @Override
    public BufferedImage createQrCode(String content) {
        int width = 200;//二维码宽度
        int height = 200;//二维码高度
        return createQrCode(content, width, height);
    }
}
