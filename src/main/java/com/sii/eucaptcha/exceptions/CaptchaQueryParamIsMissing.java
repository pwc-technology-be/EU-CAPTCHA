package com.sii.eucaptcha.exceptions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CaptchaQueryParamIsMissing extends NullPointerException{
    private  String EXEPTION_MESSAGE = "the captcha query does not have a parameter or more for the given captcha type";

    private String captchaType ;
    private List<String> missingParameters ;

    public CaptchaQueryParamIsMissing(){
         super();
    }

    public CaptchaQueryParamIsMissing(String captchaType ,String ...missingParameters) {
        this.captchaType = captchaType;
        this.missingParameters = Arrays.stream(missingParameters)
                .collect(Collectors.toList());

        this.EXEPTION_MESSAGE = "the captcha query does not have one or more of folowing parameters " + this.missingParameters.toString() + " for the captchaType = " + captchaType ;

    }

    @Override
    public String getMessage() {
        return EXEPTION_MESSAGE ;
    }


}
