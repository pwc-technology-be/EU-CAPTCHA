package com.sii.eucaptcha.security;

import com.sii.eucaptcha.controller.constants.CaptchaConstants;
import com.sii.eucaptcha.exceptions.WrongCaptchaRotationDegree;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

@Slf4j
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
            
            log.debug("min = {}" , randomRangeMin);
            log.debug("max = {}" , randomRangeMax);
            int randomNumber = (int) (Math.random() * randomRangeMax) + randomRangeMin;
            log.debug("randomNumber = {}" , randomNumber);
            log.debug("degree = {}" , degree);

            return randomNumber * degree;
        }
    }
}
