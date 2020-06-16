package com.sii.EuCaptcha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class CaptchaWelcom {

    @GetMapping("/euCaptcha")
    public String welcomPage(){
        return "EuCaptcha";
    }
}
