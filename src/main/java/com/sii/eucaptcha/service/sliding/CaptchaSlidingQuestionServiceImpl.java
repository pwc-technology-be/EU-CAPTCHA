package com.sii.eucaptcha.service.sliding;

import com.sii.eucaptcha.captcha.util.ResourceI18nMapUtil;
import com.sii.eucaptcha.security.CaptchaRandom;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CaptchaSlidingQuestionServiceImpl implements CaptchaSlidingQuestionService {

    private static final Random RANDOM = CaptchaRandom.getSecureInstance();

    private int randomIndex;

    @Override
    public String generateRandomQuestion(Locale locale) {
        Map<String, String> localesMap = new ResourceI18nMapUtil().questionMap(locale);
        return selectRandomQuestion(localesMap);
    }

    @Override
    public int[] generateRandomNumbers() {
        int[] randomNumbers = new int[2];
        int maxNumber = generateRandomNumber();
        int minNumber = generateRandomNumber();
        if(maxNumber > minNumber) {
            randomNumbers[0] = minNumber;
            randomNumbers[1] = maxNumber;
        } else if (maxNumber < minNumber) {
            randomNumbers[0] = maxNumber;
            randomNumbers[1] = minNumber;
        } else {
            randomNumbers[0] = minNumber;
            randomNumbers[1] = maxNumber + 10;
        }
        return randomNumbers;
    }

    private int generateRandomNumber() {
        int upperbound = 190;
        return RANDOM.nextInt(upperbound);
    }

    private String selectRandomQuestion(Map<String, String> localizedQuestions) {
        List<String> valuesList = new ArrayList<>(localizedQuestions.values());
        randomIndex = RANDOM.nextInt(valuesList.size());
        return valuesList.get(randomIndex);
    }

    public int getRandomIndex() {
        return randomIndex;
    }
}
