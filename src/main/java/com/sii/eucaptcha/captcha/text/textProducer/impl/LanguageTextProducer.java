package com.sii.eucaptcha.captcha.text.textProducer.impl;

import com.sii.eucaptcha.captcha.text.textProducer.TextProducer;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Locale;

/**
 * @author mousab.aidoud
 * @version 1.0
 */
public class LanguageTextProducer implements TextProducer {

    /**
     * List of characters for the 24 language
     */


    /**
     * List of common characters for LATVIAN , LITHUANIAN , SLOVENIAN , MALTESE , POLISH , CROITE
     */
    private final static char[] LIST_CHARS_COMMUN_LATVIAN_LITHUANIAN_SLOVINAN_MALTESS_POLISH_CROITE = {
            'a', 'b', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'r', 's', 't', 'u', 'v', 'z',
            'A', 'B', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'R', 'S', 'T',
            'U', 'V', 'Z'};

    private final static char[] CROATE_CHARS = ArrayUtils.addAll(
            LIST_CHARS_COMMUN_LATVIAN_LITHUANIAN_SLOVINAN_MALTESS_POLISH_CROITE,
            'Č', 'Ć', 'Đ', 'Š', 'Ž', 'č', 'ć', 'đ',
            'š', 'ž');

    private final static char[] CZECH_CHARS = ArrayUtils.addAll(ENGLISH_CHARS, 'Á', 'á', 'Č', 'č',
            'Ď', 'ď', 'É', 'é', 'Í', 'í', 'Ň',
            'ň', 'Ó', 'ó', 'Ř', 'ř', 'Š', 'š', 'Ť',
            'ť', 'Ú', 'ú', 'Ý', 'ý', 'Ž', 'ž');

   private final static char[] GREEK_CHARS = {
            'A', 'α', 'B', 'β', 'Γ', 'γ', 'Δ', 'δ', 'E', 'ε',
            'Z', 'ζ', 'H', 'η', 'Θ', 'θ', 'I', 'ι', 'K', 'κ',
            'Λ', 'λ', 'M', 'μ', 'N', 'ν', 'Ξ', 'ξ', 'O', 'ο',
            'Π', 'π', 'P', 'ρ', 'Σ', 'σ', 'T', 'τ', 'Y', 'υ',
            'Φ', 'φ', 'X', 'χ', 'Ψ', 'ψ', 'Ω', 'ω'};

    private final static char[] LATVIAN_CHARS = ArrayUtils.addAll(LIST_CHARS_COMMUN_LATVIAN_LITHUANIAN_SLOVINAN_MALTESS_POLISH_CROITE,
            'Ā', 'Č', 'Ē', 'Ģ', 'Ī', 'Ķ', 'Ļ', 'Ņ', 'Š', 'Ū', 'Ž',
            'ā', 'č', 'ē', 'ģ', 'ī', 'ķ', 'ļ', 'ņ', 'š', 'ū', 'ž', 'C', 'c');

    private final static char[] LITHUANIAN_CHARS = ArrayUtils.addAll(LIST_CHARS_COMMUN_LATVIAN_LITHUANIAN_SLOVINAN_MALTESS_POLISH_CROITE,
            'Ą', 'Č', 'Ę', 'Ė', 'Į', 'Š', 'Ų', 'Ū', 'Ž',
            'ą', 'č', 'ę', 'ė', 'į', 'š', 'ų', 'ū', 'ž', 'c', 'C');

    private final static char[] MALTESE_CHARS = new char[] {'a', 'b', 'd', 'e', 'f', 'g', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'r', 's', 't', 'u', 'v', 'z',
            'A', 'B', 'D', 'E', 'F', 'G',  'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'R', 'S', 'T',
            'U', 'V', 'Z', 'Ċ', 'Ġ', 'Ħ', 'Ż', 'ċ', 'ġ', 'ħ', 'z', 'ż'};
    private final static char[] POLISH_CHARS = ArrayUtils.addAll(LIST_CHARS_COMMUN_LATVIAN_LITHUANIAN_SLOVINAN_MALTESS_POLISH_CROITE,
            'Ą', 'Ć', 'Ę', 'Ł', 'Ń', 'Ó', 'Ś', 'Ź', 'Ż',
            'ą', 'ć', 'ę', 'ł', 'ń', 'ó', 'ś', 'Y', 'y', 'ź', 'ż');

    private final static char[] SLOVAKIAN_CHARS = ArrayUtils.addAll(ENGLISH_CHARS,
            'Ä', 'Č', 'Ď', 'Í', 'Ľ', 'Ô', 'Š', 'Ť', 'Ž',
            'ä', 'č', 'ď', 'í', 'ľ', 'ô', 'š', 'ť', 'ž');

    private final static char[] SLOVENIAN_CHARS = ArrayUtils.addAll(LIST_CHARS_COMMUN_LATVIAN_LITHUANIAN_SLOVINAN_MALTESS_POLISH_CROITE,
            'Č', 'č', 'c', 'C', 'Š', 'š', 'Ž', 'ž');

   private TextProducer txtProd;

    /**
     *
     * @param length the number of characters in the Captcha
     * @param locale the chosen locale
     * @return Text Producer based on the language selected .
     */
    public final TextProducer getLanguageTextProducer(int length, Locale locale) {
        switch (locale.getLanguage()) {

            case "hr":
                txtProd = new DefaultTextProducer(length, CROATE_CHARS);
                break;
            case "el":
                txtProd = new DefaultTextProducer(length, GREEK_CHARS);
                break;
            case "lv":
                txtProd = new DefaultTextProducer(length, LATVIAN_CHARS);
                break;
            case "lt":
                txtProd = new DefaultTextProducer(length, LITHUANIAN_CHARS);
                break;
            case "mt":
                txtProd = new DefaultTextProducer(length, MALTESE_CHARS);
                break;
            case "pl":
                txtProd = new DefaultTextProducer(length, POLISH_CHARS);
                break;
            case "sk":
                txtProd = new DefaultTextProducer(length, SLOVAKIAN_CHARS);
                break;
            case "sl":
                txtProd = new DefaultTextProducer(length, SLOVENIAN_CHARS);
                break;
            case "cs":
                txtProd = new DefaultTextProducer(length, CZECH_CHARS);
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
