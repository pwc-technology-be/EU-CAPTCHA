package com.sii.eucaptcha.captcha.text.textProducer.impl;

import com.sii.eucaptcha.captcha.text.textProducer.TextProducer;
import com.sii.eucaptcha.security.CaptchaRandom;

import java.util.Random;
import java.util.Set;

public class DefaultTextProducer implements TextProducer {

    private static final Random RANDOM = CaptchaRandom.getSecureInstance();

    private final int LENGTH;
    private final Set<String> LOCALIZED_CHARACTERS;

        public DefaultTextProducer(int length, Set<String> localizedCharacters) {
        LENGTH = length;
        LOCALIZED_CHARACTERS = localizedCharacters;
    }

    @Override
    public String getText() {
        StringBuilder capText = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            capText.append(getRandomSetElement());
            capText.append(" ");
        }
        return capText.toString();
    }

    public String getRandomSetElement() {
        return LOCALIZED_CHARACTERS.stream().skip(RANDOM.nextInt(LOCALIZED_CHARACTERS.size())).findFirst().orElse(null);
    }
}
