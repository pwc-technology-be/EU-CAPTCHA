package com.sii.eucaptcha.captcha.util;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class ResourceI18nMapUtil {

    public final Map<String, String> voiceMap(Locale locale) {
        Map<String, String> voicesMap = new HashMap<>();
        ResourceBundle labels = ResourceBundle.getBundle("messages", locale);
        Enumeration<String> bundleKeys = labels.getKeys();
        while (bundleKeys.hasMoreElements()) {
            String key = bundleKeys.nextElement();
            String value = labels.getString(key);
            if (key.length() == 1 && StringUtils.isNotBlank(value) && StringUtils.contains(value, locale.getLanguage())) {
                voicesMap.put(key, value);
            }
        }
        return voicesMap;
    }

    public final Map<String, String> questionMap(Locale locale) {
        Map<String, String> voicesMap = new HashMap<>();
        ResourceBundle labels = ResourceBundle.getBundle("messages", locale);
        Enumeration<String> bundleKeys = labels.getKeys();
        while (bundleKeys.hasMoreElements()) {
            String key = bundleKeys.nextElement();
            String value = labels.getString(key);
            if (StringUtils.isNotBlank(value) && StringUtils.startsWith(key, "question")) {
                voicesMap.put(key, value);
            }
        }
        return voicesMap;
    }
}
