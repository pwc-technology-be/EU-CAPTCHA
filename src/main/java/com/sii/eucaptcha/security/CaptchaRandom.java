package com.sii.eucaptcha.security;

import java.security.SecureRandom;

public class CaptchaRandom {

    private static SecureRandom SECURERANDOM;

    private CaptchaRandom() {
    }

    public static SecureRandom getSecureInstance() {
        if(SECURERANDOM == null) {
            SECURERANDOM = new SecureRandom();
        }
        return SECURERANDOM;
    }
}
