package com.sii.EuCaptcha.text.textProducer;

import nl.captcha.text.producer.DefaultTextProducer;
import nl.captcha.text.producer.TextProducer;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Locale;

public class LanguageTextProducer implements TextProducer {

    private final static char[] ENGLISH_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'};

    private final static char[] LIST_CHARS_COMMUN_LATVIAN_LITHUANIAN_SLOVINAN_MALTESS_POLISH_CROITE = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'r', 's', 't', 'u', 'v', 'z',
            'A', 'B', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'R', 'S', 'T',
            'U', 'V', 'Z'};
    // NO synthése local in google transqlte
    private final static char[] BULGARE_CHARS = {
            'А', 'а', 'Б', 'б', 'В', 'в', 'Г', 'г', 'Д', 'д', 'Е', 'е', 'Ж', 'ж', 'З', 'з', 'И', 'и',
            'Й', 'й', 'К', 'к', 'Л', 'л', 'М', 'м', 'Н', 'н', 'О', 'о', 'П', 'п', 'Р', 'р', 'С', 'с',
            'Т', 'т', 'У', 'у', 'Ф', 'ф', 'Х', 'х', 'Ц', 'ц', 'Ч', 'ч', 'Ш', 'ш', 'Щ', 'щ',
            'Ю', 'ю', 'Я', 'я', '1', '2', '3', '4', '6', '7', '8', '9'};

    private final static char[] CROATE_CHARS = ArrayUtils.addAll(LIST_CHARS_COMMUN_LATVIAN_LITHUANIAN_SLOVINAN_MALTESS_POLISH_CROITE,
            'Č', 'Ć', 'Đ', 'Š', 'Ž', 'č', 'ć', 'đ',
            'š', 'ž');

    private final static char[] CZECH_CHARS = ArrayUtils.addAll(ENGLISH_CHARS, 'Á', 'á', 'Č', 'č',
            'Ď', 'ď', 'É', 'é', 'ě', 'Í', 'í', 'Ň',
            'ň', 'Ó', 'ó', 'Ř', 'ř', 'Š', 'š', 'Ť',
            'ť', 'Ú', 'ú', 'ů', 'Ý', 'ý', 'Ž', 'ž');

    private final static char[] DANISH_CHARS = ArrayUtils.addAll(ENGLISH_CHARS, 'Æ', 'æ', 'Ø', 'ø', 'Å', 'å');

    private final static char[] SPANISH_CHARS = ArrayUtils.addAll(ENGLISH_CHARS, 'Ñ', 'ñ');

    private final static char[] ESTONIAN_CHARS = ArrayUtils.addAll(ENGLISH_CHARS, 'š', 'ž', 'õ', 'ä', 'ö', 'ü', 'Š', 'Ž', 'Õ', 'Ä', 'Ö', 'Ü');

    private final static char[] FINNISH_CHARS = ArrayUtils.addAll(ENGLISH_CHARS, 'Å', 'å', 'Ä', 'ä', 'Ö', 'ö');

    private final static char[] GERMAN_CHARS = ArrayUtils.addAll(ENGLISH_CHARS, 'Ä', 'Ö', 'Ü', 'ß', 'ä', 'ö', 'ü', 'ß');

    private final static char[] GREEK_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'Α', 'α', 'Β', 'β', 'Γ', 'γ', 'Δ', 'δ', 'Ε', 'ε',
            'Ζ', 'ζ', 'Η', 'η', 'Θ', 'θ', 'Ι', 'ι', 'Κ', 'κ',
            'Λ', 'λ', 'Μ', 'μ', 'Ν', 'ν', 'Ξ', 'ξ', 'Ο', 'ο',
            'Π', 'π', 'Ρ', 'ρ', 'Σ', 'σ', 'Τ', 'τ', 'Υ', 'υ',
            'Φ', 'φ', 'Χ', 'χ', 'Ψ', 'ψ', 'Ω', 'ω'};

    private final static char[] HUNGARIAN_CHARS = ArrayUtils.addAll(ENGLISH_CHARS,
            'Á', 'á', 'É', 'é', 'Í', 'í', 'Ó', 'ó', 'Ö', 'ö',
            'Ő', 'ő', 'Ú', 'ú', 'Ü', 'ü', 'Ű', 'ű');
    // NO synthése local in google transqlte----------------------------------------------------------------------------------------------
    private final static char[] IRISH_CHARS = ArrayUtils.addAll(ENGLISH_CHARS, 'á', 'é', 'í', 'ó', 'ú', 'Á', 'É', 'Í', 'Ó', 'Ú');
    //-----------------------------------------------------------------------------------------------------------------------------------------
    private final static char[] LATVIAN_CHARS = ArrayUtils.addAll(LIST_CHARS_COMMUN_LATVIAN_LITHUANIAN_SLOVINAN_MALTESS_POLISH_CROITE,
            'Ā', 'Č', 'Ē', 'Ģ', 'Ī', 'Ķ', 'Ļ', 'Ņ', 'Š', 'Ū', 'Ž',
                    'ā', 'č', 'ē', 'ģ', 'ī', 'ķ', 'ļ', 'ņ', 'š', 'ū', 'ž' , 'C','c');


    private final static char[] LITHUANIAN_CHARS = ArrayUtils.addAll(LIST_CHARS_COMMUN_LATVIAN_LITHUANIAN_SLOVINAN_MALTESS_POLISH_CROITE,
            'Ą', 'Č', 'Ę', 'Ė', 'Į', 'Š', 'Ų', 'Ū', 'Ž',
                    'ą', 'č', 'ę', 'ė', 'į', 'š', 'ų', 'ū', 'ž' , 'c', 'C');

    // NO synthése local in google transqlte----------------------------------------------------------------------------------------
    private final static char[] MALTESE_CHARS = ArrayUtils.addAll(LIST_CHARS_COMMUN_LATVIAN_LITHUANIAN_SLOVINAN_MALTESS_POLISH_CROITE,
            'Ċ', 'Ġ', 'Ħ', 'Ż', 'ċ', 'ġ', 'ħ', 'z', 'ż');
    //--------------------------------------------------------------------------------------------------------------------------------
    private final static char[] POLISH_CHARS = ArrayUtils.addAll(LIST_CHARS_COMMUN_LATVIAN_LITHUANIAN_SLOVINAN_MALTESS_POLISH_CROITE,
                'Ą', 'Ć', 'Ę', 'Ł', 'Ń', 'Ó', 'Ś', 'Ź', 'Ż',
                    'ą', 'ć', 'ę', 'ł', 'ń', 'ó', 'ś', 'Y', 'y', 'ź', 'ż');

    private final static char[] ROMANIAN_CHARS = ArrayUtils.addAll(ENGLISH_CHARS, 'Ă', 'ă', 'Â', 'â', 'Î', 'î', 'Ș', 'ș', 'Ț', 'ț');

    private final static char[] SLOVAKIAN_CHARS = ArrayUtils.addAll(ENGLISH_CHARS,
                'Ä', 'Č', 'Ď', 'Í', 'Ľ', 'Ô', 'Š', 'Ť','Ž',
                        'ä', 'č', 'ď', 'í', 'ľ', 'ô', 'š', 'ť','ž');
    // NO synthése local in google transqlte----------------------------------------------------------------------------------------
    private final static char[] SLOVENIAN_CHARS = ArrayUtils.addAll(LIST_CHARS_COMMUN_LATVIAN_LITHUANIAN_SLOVINAN_MALTESS_POLISH_CROITE,
            'Č', 'č', 'c', 'C', 'Š', 'š', 'Ž', 'ž');
   //-----------------------------------------------------------------------------------------------------------------------------------------
    private final static char[] SWEDISH_CHARS = ArrayUtils.addAll(ENGLISH_CHARS, 'Å', 'å', 'Ä', 'ä', 'Ö', 'ö');

    private  TextProducer _txtProd;
    
    public final TextProducer LanguageTextProducer(int length, Locale locale) {

        switch (locale.getLanguage()) {
            case "en":
                _txtProd = new DefaultTextProducer(length, ENGLISH_CHARS);
                break;
            case "fr":
                _txtProd = new DefaultTextProducer(length, ENGLISH_CHARS);
                break;
            case "de":
                _txtProd = new DefaultTextProducer(length, GERMAN_CHARS);
                break;
            case "bg":
                _txtProd = new DefaultTextProducer(length, BULGARE_CHARS);
                break;
            case "hr":
                _txtProd = new DefaultTextProducer(length, CROATE_CHARS);
                break;
            case "da":
                _txtProd = new DefaultTextProducer(length, DANISH_CHARS);
                break;
            case "es":
                _txtProd = new DefaultTextProducer(length, SPANISH_CHARS);
                break;
            case "et":
                _txtProd = new DefaultTextProducer(length, ESTONIAN_CHARS);
                break;
            case "fi":
                _txtProd = new DefaultTextProducer(length, FINNISH_CHARS);
                break;
            case "el":
                _txtProd = new DefaultTextProducer(length, GREEK_CHARS);
                break;
            case "hu":
                _txtProd = new DefaultTextProducer(length, HUNGARIAN_CHARS);
                break;
            case "ga":
                _txtProd = new DefaultTextProducer(length, IRISH_CHARS);
                break;
            case "it":
                _txtProd = new DefaultTextProducer(length, ENGLISH_CHARS);
                break;
            case "lv":
                _txtProd = new DefaultTextProducer(length, LATVIAN_CHARS);
                break;
            case "lt":
                _txtProd = new DefaultTextProducer(length, LITHUANIAN_CHARS);
                break;
            case "mt":
                _txtProd = new DefaultTextProducer(length, MALTESE_CHARS);
                break;
            case "nl":
                _txtProd = new DefaultTextProducer(length, ENGLISH_CHARS);
                break;
            case "pl":
                _txtProd = new DefaultTextProducer(length, POLISH_CHARS);
                break;
            case "pt":
                _txtProd = new DefaultTextProducer(length, ENGLISH_CHARS);
                break;
            case "ro":
                _txtProd = new DefaultTextProducer(length, ROMANIAN_CHARS);
                break;
            case "sk":
                _txtProd = new DefaultTextProducer(length, SLOVAKIAN_CHARS);
                break;
            case "sl":
                _txtProd = new DefaultTextProducer(length, SLOVENIAN_CHARS);
            case "sv":
                _txtProd = new DefaultTextProducer(length, SWEDISH_CHARS);
                break;
            case "cs":
                _txtProd = new DefaultTextProducer(length, CZECH_CHARS);
                break;
            default:
                _txtProd = new DefaultTextProducer(length, ENGLISH_CHARS);
        }
           return this._txtProd;
    }


    @Override
    public final String getText() {
        return new StringBuffer(_txtProd.getText()).reverse().toString();
    }
}
