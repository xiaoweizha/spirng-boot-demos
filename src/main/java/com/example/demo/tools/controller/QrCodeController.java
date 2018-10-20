package com.example.demo.tools.controller;

import com.example.demo.config.aspect.annotation.AccessLimit;
import com.example.demo.tools.service.QRCodeHandlerService;
import com.example.demo.tools.service.QRCodeWithPictureService;
import com.example.demo.tools.service.QrCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@Api(description = "基础 -- 二维码")
@Controller
@RequestMapping("qrcode")
public class QrCodeController {

    @Resource
    private QrCodeService qrCodeService;
    @Resource
    private QRCodeHandlerService qrCodeHandlerService;
    @Resource
    private QRCodeWithPictureService qrCodeWithPictureService;

    @AccessLimit
    @ApiOperation(value = "生成二维码", notes = "生成二维码")
    @RequestMapping(value = "/othersImg", method = {RequestMethod.GET})
    public void createQrCode(HttpServletResponse response, @RequestParam String content) {
        BufferedImage bufferedImage = qrCodeService.createQrCode(content);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ImageIO.write(bufferedImage, "jpeg", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @AccessLimit
    @ApiOperation(value = "生成二维码", notes = "生成二维码")
    @RequestMapping(value = "/myImg", method = {RequestMethod.GET})
    public void createQrCode(HttpServletResponse resp) {
        try {
            qrCodeHandlerService.encoderQRCode("https://github.com/zhzhair/", resp.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AccessLimit
    @ApiOperation(value = "生成带图片的二维码", notes = "生成生成带图片的二维码")
    @RequestMapping(value = "/imgWithPic", method = {RequestMethod.GET})
    public void showImgCodeWithPicture(HttpServletResponse resp) {
        try {
            //QRCodeWithPicture.encode("http://www.baidu.com", req.getServletContext().getRealPath("/uploadpic/hehe.jpg"), resp.getOutputStream(), true);
            qrCodeWithPictureService.encode("http://www.baidu.com", "F:/spring-boot-demos/spring-boot-app/pictures/java.bmp", resp.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
