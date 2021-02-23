package com.sii.eucaptcha.captcha.text.textProducer.impl;

import com.sii.eucaptcha.captcha.text.textProducer.TextProducer;
import com.sii.eucaptcha.security.CaptchaRandom;

import java.security.SecureRandom;
import java.util.Random;

public class DefaultTextProducer implements TextProducer {

    private static final Random RANDOM = CaptchaRandom.getSecureInstance();
    private static final int DEFAULT_LENGTH = 5;
    private static final char[] DEFAULT_CHARS = new char[] { 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'k', 'm', 'n', 'p', 'r', 'w', 'x', 'y',
            '2', '3', '4', '5', '6', '7', '8', };

    private final int LENGTH;
    private final char[] SRC_CHARS;

    public DefaultTextProducer() {
        this(DEFAULT_LENGTH, DEFAULT_CHARS);
    }

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
