package com.sii.EuCaptcha.voice;

import nl.captcha.audio.Sample;
import nl.captcha.audio.producer.VoiceProducer;
import nl.captcha.util.FileUtil;
import java.util.Map;

public class LanguageVoiceProducer implements VoiceProducer {

    private String answer;

    public LanguageVoiceProducer(String textProducer, Map<String, String> voices){
        this.answer = textProducer;
        _voices = voices;
    }

    private Map<String, String> _voices;

    public LanguageVoiceProducer(Map<String, String> voices) {
        _voices = voices;
    }

    public Sample getVocalization(char num) {
        String str = String.valueOf(num);
        if (!isUpperCase(str)){
            str = str.toUpperCase();
        }
        String filename = _voices.get(str);
        return FileUtil.readSample(filename);
    }

    public static boolean isUpperCase(String s)
    {
        for (int i=0; i<s.length(); i++)
        {
            if (Character.isLowerCase(s.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }



}

