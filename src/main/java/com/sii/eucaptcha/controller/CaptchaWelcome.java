package com.sii.eucaptcha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;
/**
 * @author mousab.aidoud
 * @version 1.0
 * Get the captcha welcome page .
 */
@Controller
@ApiIgnore
public class CaptchaWelcome {
    /**
     *
     * @return JSP FILE FOR THE DEMO
     * (     server:port/euCaptcha    )
     */
    @GetMapping("/")
    public String welcomePage(){
        return "EUCAPTCHA";
    }
}
