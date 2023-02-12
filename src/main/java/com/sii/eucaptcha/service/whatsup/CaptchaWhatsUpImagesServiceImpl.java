package com.sii.eucaptcha.service.whatsup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
public class CaptchaWhatsUpImagesServiceImpl implements CaptchaWhatsUpImagesService{

    @Autowired
    private ResourceLoader resourceLoader ;

    @Override
    public Resource loadRandomImage() {
        int nameRangeMin = 1;
        int nameRangeMax = 19;
        int randomNum = nameRangeMin + (int)(Math.random() * nameRangeMax);
        return loadImage(Integer.toString(randomNum));
    }

    @Override
    public BufferedImage rotate(BufferedImage bimg, double angle) {
        int w = bimg.getWidth();
        int h = bimg.getHeight();
        BufferedImage rotated = new BufferedImage(w, h, bimg.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(angle), w/2, h/2);
        graphic.drawImage(bimg, null, 0, 0);
        graphic.dispose();
        return rotated;
    }

    private Resource loadImage(String name) {
        return resourceLoader.getResource(
                "classpath:captchaImages/"+name+".png");
    }
}
