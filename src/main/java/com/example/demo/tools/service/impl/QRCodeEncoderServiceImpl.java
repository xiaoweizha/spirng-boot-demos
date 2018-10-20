package com.example.demo.tools.service.impl;

import com.example.demo.tools.service.QRCodeHandlerService;
import com.swetake.util.Qrcode;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

@Service
public class QRCodeEncoderServiceImpl implements QRCodeHandlerService {

	@Override
	public void encoderQRCode(String content, OutputStream outputStream) {
		try {
			Qrcode qrcodeHandler = new Qrcode();
			// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
			qrcodeHandler.setQrcodeErrorCorrect('M');
			qrcodeHandler.setQrcodeEncodeMode('B');
			qrcodeHandler.setQrcodeVersion(5);
			byte[] contentBytes = content.getBytes("gb2312");
			BufferedImage bufImg = new BufferedImage(190,190,BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();
			gs.setBackground(Color.WHITE);
			gs.clearRect(0, 0, 190, 190);
			// 设定图像颜色> BLACK
			gs.setColor(Color.BLACK);
			// 设置偏移量 不设置可能导致解析出错
			int pixoff = 2;
			// 输出内容> 二维码
			if (contentBytes.length > 0 && contentBytes.length < 800) {
				boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							gs.fillRect(j * 5 + pixoff, i * 5 + pixoff, 5, 5);
						}
					}
				}
			} else {
			}
			gs.dispose();
			bufImg.flush();
			ImageIO.write(bufImg, "png", outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
