package com.sii.eucaptcha.exceptions;

public class CaptchaQueryIsNull extends NullPointerException{
    private final  String EXEPTION_MESSAGE = "CaptchaQueryIdNull or missing";

    @Override
    public String getMessage() {
        return EXEPTION_MESSAGE +super.getMessage();
    }
}
