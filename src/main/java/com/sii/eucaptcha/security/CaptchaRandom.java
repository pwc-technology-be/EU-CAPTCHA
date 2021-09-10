package com.sii.eucaptcha.security;

import com.sii.eucaptcha.controller.constants.CaptchaConstants;
import com.sii.eucaptcha.exceptions.WrongCaptchaRotationDegree;

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

    public static int getRandomRotationAngle(int degree) throws WrongCaptchaRotationDegree {
        if(degree < CaptchaConstants.MIN_DEGREE || degree > CaptchaConstants.MAX_DEGREE)
            throw  new WrongCaptchaRotationDegree();
        else {
            int randomRangeMax = 6;
            int randomRangeMin = 1;
            int range = randomRangeMax - randomRangeMin + 1;
            System.out.println("min = " + randomRangeMin);
            System.out.println("max = " + randomRangeMax);
            int randomNumber = (int) (Math.random() * randomRangeMax) + randomRangeMin;
            System.out.println(randomNumber);
            System.out.println(degree);
            return randomNumber * degree;
        }
    }
}
