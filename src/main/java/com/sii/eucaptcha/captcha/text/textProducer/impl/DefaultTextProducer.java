package com.sii.eucaptcha.captcha.text.textProducer.impl;

import com.sii.eucaptcha.captcha.text.textProducer.TextProducer;
import com.sii.eucaptcha.security.CaptchaRandom;

import java.security.SecureRandom;
import java.util.Random;
import java.util.ResourceBundle;

public class DefaultTextProducer implements TextProducer {

    private static final Random RANDOM = CaptchaRandom.getSecureInstance();

    private final int LENGTH;
    private final char[] SRC_CHARS;

        public DefaultTextProducer(int length, char[] srcChars) {
        LENGTH = length;
        SRC_CHARS = copyOf(srcChars, srcChars.length);
    }

    @Override
    public String getText() {
        StringBuilder capText = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            capText.append(SRC_CHARS[RANDOM.nextInt(SRC_CHARS.length)]);
        }
        return capText.toString();
    }

    private static char[] copyOf(char[] original, int newLength) {
        char[] copy = new char[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }
}
