package com.sii.eucaptcha.service.whatsup;

import org.springframework.core.io.Resource;

import java.awt.image.BufferedImage;

public interface CaptchaWhatsUpImagesService {

    Resource loadRandomImage();
    BufferedImage rotate(BufferedImage bimg, double angle);

}
