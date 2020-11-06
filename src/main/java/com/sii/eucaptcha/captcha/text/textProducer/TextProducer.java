package com.sii.eucaptcha.captcha.text.textProducer;

public interface TextProducer {

    /**
     * Generate a series of characters to be used as the answer for the CAPTCHA.
     *
     * @return The answer for the CAPTCHA.
     */
    String getText();
}
