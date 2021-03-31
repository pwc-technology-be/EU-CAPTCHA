package com.sii.eucaptcha.exceptions;

public class WrongCaptchaRotationDegree extends Exception{
    private final String EX_MESSAGE = "wrrong rotation degree";

    @Override
    public String getMessage() {
        return EX_MESSAGE ;
    }
}
