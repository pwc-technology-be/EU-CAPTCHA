package com.sii.eucaptcha.exceptions;

public class CaptchaDataIsNull extends NullPointerException{
    private final  String EXEPTION_MESSAGE = "CaptchaDataIsNull or missing";

    @Override
    public String getMessage() {
        return EXEPTION_MESSAGE +super.getMessage();
    }
}
