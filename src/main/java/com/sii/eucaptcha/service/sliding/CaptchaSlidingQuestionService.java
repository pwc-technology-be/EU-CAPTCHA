package com.sii.eucaptcha.service.sliding;

import java.util.Locale;

public interface CaptchaSlidingQuestionService {
    String generateRandomQuestion(Locale locale);

    int[] generateRandomNumbers();

    int getRandomIndex();
}
