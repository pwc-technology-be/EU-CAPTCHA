package com.sii.eucaptcha.captcha.text.textProducer.impl;

import com.sii.eucaptcha.captcha.text.textProducer.TextProducer;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Locale;

/**
 * @author mousab.aidoud
 * @version 1.0
 */
public class LanguageTextProducer implements TextProducer {

    private final static char[] SLOVAKIAN_CHARS = ArrayUtils.addAll(ENGLISH_CHARS,
            'Ä', 'Č', 'Ď', 'Í', 'Ľ', 'Ô', 'Š', 'Ť', 'Ž',
            'ä', 'č', 'ď', 'í', 'ľ', 'ô', 'š', 'ť', 'ž');

    private final static char[] SLOVENIAN_CHARS = ArrayUtils.addAll(
            'a', 'b', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'r', 's', 't', 'u', 'v', 'z',
            'A', 'B', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'R', 'S', 'T',
            'U', 'V', 'Z','Č', 'č', 'c', 'C', 'Š', 'š', 'Ž', 'ž');

   private TextProducer txtProd;

    /**
     *
     * @param length the number of characters in the Captcha
     * @param locale the chosen locale
     * @return Text Producer based on the language selected .
     */
    public final TextProducer getLanguageTextProducer(int length, Locale locale) {
        switch (locale.getLanguage()) {
            case "sk":
                txtProd = new DefaultTextProducer(length, SLOVAKIAN_CHARS);
                break;
            case "sl":
                txtProd = new DefaultTextProducer(length, SLOVENIAN_CHARS);
                break;
        }
        return this.txtProd;
    }

    /**
     *
     * @return text
     */
    @Override
    public final String getText() {
        return new StringBuffer(txtProd.getText()).reverse().toString();
    }
}
