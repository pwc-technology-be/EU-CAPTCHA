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
            if (key.startsWith("alphabet.") && StringUtils.isNotBlank(value)) {
                voicesMap.put(key.substring(9), value);
            }
        }
        return voicesMap;
    }
}
