package com.sii.EuCaptcha.voice;

import nl.captcha.audio.Sample;
import nl.captcha.audio.producer.VoiceProducer;
import nl.captcha.util.FileUtil;
import java.util.Map;

/**
 * @author mousab.aidoud
 * @version 1.0
 * Language voice producer
 */
public class LanguageVoiceProducer implements VoiceProducer {


    private String answer;

    /**
     *
     * @param textProducer
     * @param voices
     */
    public LanguageVoiceProducer(String textProducer, Map<String, String> voices){
        this.answer = textProducer;
        _voices = voices;
    }

    private Map<String, String> _voices;

    public LanguageVoiceProducer(Map<String, String> voices) {
        _voices = voices;
    }

    /**
     * Listing of the audio files for each characters , and convert the small case tio upper case
     * @param num
     * @return audio file for each char
     */
    public Sample getVocalization(char num) {
        String str = String.valueOf(num);
        if (!isUpperCase(str)){
            str = str.toUpperCase();
        }
        String filename = _voices.get(str);
        return FileUtil.readSample(filename);
    }

    /**
     *
     * @param s
     * @return true|false based on the case for the charter
     */
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

