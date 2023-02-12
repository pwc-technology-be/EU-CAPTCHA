package com.sii.eucaptcha.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author mousab.aidoud
 * @version 1.0
 * Get the captcha welcome page .
 */
@Controller
public class CaptchaWelcome {

    @Value("${controller.captcha.welcome}")
    private String welcomePageName;

    @Value("${controller.captcha.textualCaptcha}")
    private String textualCaptchaPageName;
    /**
     *
     * @return JSP FILE FOR THE DEMO
     * (     server:port/euCaptcha    )
     */

    @Value("${controller.captcha.rotationalCaptcha}")
    private String rotationalCaptchaPageName;

    @Value("${controller.captcha.slidingCaptcha}")
    private String slidingCaptchaPageName;

    /**
     *
     * @return JSP FILE FOR THE DEMO
     * (     server:port/euCaptcha    )
     */
    @GetMapping("/")
    public String welcomePage() {return welcomePageName; }

    @GetMapping("/textual")
    public String textualCaptchaPage(){
        return textualCaptchaPageName;
    }

    @GetMapping("/rotate")
    public String rotateCaptchaPage(){
        return rotationalCaptchaPageName;
    }

    @GetMapping("/sliding")
    public String slidingCaptchaPage() {return slidingCaptchaPageName;}
}
