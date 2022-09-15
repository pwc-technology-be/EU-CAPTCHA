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
    public int generateMaxNumber() {
        int upperbound = 1000;
        return RANDOM.nextInt(upperbound);
    }

    @Override
    public int generateMinNumber(int maxnumber) {
        return RANDOM.nextInt(maxnumber-10);
    }

    private String selectRandomQuestion(Map<String, String> localizedQuestions) {
        List<String> valuesList = new ArrayList<String>(localizedQuestions.values());
        randomIndex = RANDOM.nextInt(valuesList.size());
        return valuesList.get(randomIndex);
    }

    public int getRandomIndex() {
        return randomIndex;
    }
}
