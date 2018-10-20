package com.example.demo.tools.service.impl;

import com.example.demo.common.constant.ImgCheckCodeParams;
import com.example.demo.tools.service.ImgCheckCodeService;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @author Created by zhaozh01 on 2017/10/20.
 */
@Service
public class ImgCheckCodeServiceImpl implements ImgCheckCodeService {

	@Override
    public String codeCreate(OutputStream outputStream){
        /*获取画笔*/
        BufferedImage bufferedImage = new BufferedImage(ImgCheckCodeParams.WIDTH, ImgCheckCodeParams.HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        /*填充图片区域和背景图片*/
        int grayIndex = ImgCheckCodeParams.GRAY;
        graphics2D.setColor(new Color(grayIndex, grayIndex, grayIndex));
        graphics2D.fillRect(0, 0, ImgCheckCodeParams.WIDTH, ImgCheckCodeParams.HEIGHT);

        /*画干扰点*/
        Color colorDots = new Color(new Random().nextInt(256),new Random().nextInt(256),new Random().nextInt(256));
        for (int i = 0; i < ImgCheckCodeParams.DOTS_SUM; i++) {
            int widthPoint = new Random().nextInt(ImgCheckCodeParams.WIDTH);
            int heightPoint = new Random().nextInt(ImgCheckCodeParams.HEIGHT);
            graphics2D.fillRect(widthPoint,heightPoint,ImgCheckCodeParams.DOTS_LEN,ImgCheckCodeParams.DOTS_LEN);
            graphics2D.setColor(colorDots);
        }
        /*填充字符串*/
        StringBuilder stringBuilder = new StringBuilder();
        Color colorCode = this.getColor();
        colorCode.brighter();
        for (int i = 0; i < ImgCheckCodeParams.CODE_COUNT; i++) {
            graphics2D.setFont(new Font(ImgCheckCodeParams.CHECK_CODE_FACE, Font.BOLD, ImgCheckCodeParams.FONT_SIZE));
            graphics2D.setColor(colorCode);
            String str = ImgCheckCodeParams.CHECK_CODE_STR.charAt(new Random().nextInt(ImgCheckCodeParams.CHECK_CODE_STR.length()))+"";
            int lr = ImgCheckCodeParams.MARGIN_LEFT+ImgCheckCodeParams.PADDING_RIGHT*i + this.getRandomNum(ImgCheckCodeParams.WAVE_LEFT_RIGHT);
            int tb = ImgCheckCodeParams.BOTTOM_TO_TOP + ImgCheckCodeParams.WAVE_BOTTOM_TOP;
            graphics2D.drawString(str, lr, tb);
            stringBuilder.append(str);
        }
        
        graphics2D.dispose();
        try {//将验证码图片作为输出流输出到html页面
            ImageIO.write(bufferedImage, "JPEG", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回较大字体的字符拼接的字符串
        return stringBuilder.toString();
    }

    private int getRandomNum(int n){
        float rand = new Random().nextInt(n);
        float fnum = -0.5f + rand/n;
        return Math.round(fnum * n);
    }

    private Color getColor(){
        int n = new Random().nextInt(10);
        switch (n){
            case 0 : return new Color(255, 246, 4);
            case 1 : return new Color(255, 201, 5);
            case 2 : return new Color(255, 121, 10);
            case 3 : return new Color(208, 255, 147);
            case 4 : return new Color(1, 255, 204);
            case 5 : return new Color(19, 255, 60);
            case 6 : return new Color(179, 255, 11);
            case 7 : return new Color(255, 149, 232);
            case 8 : return new Color(181, 173, 254);
            default : return new Color(255, 249, 206);
        }
    }
}
