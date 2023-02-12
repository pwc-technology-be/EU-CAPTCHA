package com.sii.eucaptcha.service;

import com.ibm.icu.text.RuleBasedNumberFormat;
import com.sii.eucaptcha.captcha.Captcha;
import com.sii.eucaptcha.captcha.audio.Sample;
import com.sii.eucaptcha.captcha.audio.noise.impl.EuCaptchaNoiseProducer;
import com.sii.eucaptcha.captcha.audio.voice.VoiceProducer;
import com.sii.eucaptcha.captcha.audio.voice.impl.LanguageVoiceProducer;
import com.sii.eucaptcha.captcha.text.image.background.impl.GradiatedBackgroundProducer;
import com.sii.eucaptcha.captcha.text.image.gimpy.impl.EuCaptchaGimpyRenderer;
import com.sii.eucaptcha.captcha.text.image.noise.impl.StraightLineImageNoiseProducer;
import com.sii.eucaptcha.captcha.text.textProducer.TextProducer;
import com.sii.eucaptcha.captcha.text.textProducer.impl.DefaultTextProducer;
import com.sii.eucaptcha.captcha.text.textRender.impl.CaptchaTextRender;
import com.sii.eucaptcha.captcha.util.ResourceI18nMapUtil;
import com.sii.eucaptcha.configuration.properties.SoundConfigProperties;
import com.sii.eucaptcha.controller.constants.CaptchaConstants;
import com.sii.eucaptcha.controller.dto.captchaquery.CaptchaQueryDto;
import com.sii.eucaptcha.controller.dto.captcharesult.CaptchaResultDto;
import com.sii.eucaptcha.controller.dto.captcharesult.SlidingCaptchaResultDto;
import com.sii.eucaptcha.controller.dto.captcharesult.TextualCaptchaResultDtoDto;
import com.sii.eucaptcha.controller.dto.captcharesult.WhatsUpCaptchaResultDtoDto;
import com.sii.eucaptcha.exceptions.CaptchaQueryIsNull;
import com.sii.eucaptcha.exceptions.WrongCaptchaRotationDegree;
import com.sii.eucaptcha.security.CaptchaRandom;
import com.sii.eucaptcha.service.sliding.CaptchaSlidingQuestionService;
import com.sii.eucaptcha.service.whatsup.CaptchaWhatsUpImagesService;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author mousab.aidoud
 * @version 1.0
 * Captcha Service Class
 */
@Service
@Slf4j
public class CaptchaService {
    /**
     * Parameters of Captcha {WIDTH , HEIGHT , EXPIRY TIME }
     */
    private static final int CAPTCHA_WIDTH = 400;
    private static final int CAPTCHA_HEIGHT = 200;
    private static final long CAPTCHA_EXPIRY_TIME = 5;

    /**
     * List of colors and background colors
     */
    private static final List<Color> COLORS = new ArrayList<>(3);
    private static final List<Color> COLOR_STRAIGHT_LINE_NOISE = new ArrayList<>(3);
    private static final List<Color> BACKGROUND_COLORS = new ArrayList<>(5);

    /**
     * List of fonts and font sizes
     */
    private static final Font FONTS_SERIF = new Font("Serif", Font.BOLD, 50);
    private static final Font FONTS_SANS_SERIF = new Font("SansSerif", Font.BOLD, 50);


    static {
        COLORS.add(Color.BLACK);
        COLORS.add(Color.GRAY);
        COLORS.add(Color.DARK_GRAY);

        BACKGROUND_COLORS.add(Color.PINK);
        BACKGROUND_COLORS.add(Color.ORANGE);
        BACKGROUND_COLORS.add(Color.LIGHT_GRAY);
        BACKGROUND_COLORS.add(Color.WHITE);
        BACKGROUND_COLORS.add(Color.CYAN);

        COLOR_STRAIGHT_LINE_NOISE.add(Color.RED);
        COLOR_STRAIGHT_LINE_NOISE.add(Color.ORANGE);
        COLOR_STRAIGHT_LINE_NOISE.add(Color.MAGENTA);

    }

    private List<String> fonts = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());

    /**
     * Building a map with Expiration Time CAPTCHA_EXPIRY_TIME
     */
    private static final Map<String, String> captchaCodeMap =
            ExpiringMap.builder().expiration(CAPTCHA_EXPIRY_TIME, TimeUnit.MINUTES).build();

    private final SecureRandom random = CaptchaRandom.getSecureInstance();
    private SoundConfigProperties props;
    private CaptchaWhatsUpImagesService captchaWhatsUpImagesService;

    private CaptchaSlidingQuestionService captchaSlidingQuestionService;

    private ResourceLoader resourceLoader;

    private int counter;

    public CaptchaService(CaptchaWhatsUpImagesService captchaWhatsUpImagesService,
                          CaptchaSlidingQuestionService captchaSlidingQuestionService, SoundConfigProperties props,
                          ResourceLoader resourceLoader) {
        this.captchaWhatsUpImagesService = captchaWhatsUpImagesService;
        this.captchaSlidingQuestionService = captchaSlidingQuestionService;
        this.props = props;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Generate Captcha  Image Wrapper
     *
     * @param captchaQueryDto the captcha Query that carry the query parameters
     * @return response as String contains CaptchaID and Captcha Image
     */
    public CaptchaResultDto generateCaptchaWrapper(CaptchaQueryDto captchaQueryDto) {
        if (captchaQueryDto == null) {
            throw new CaptchaQueryIsNull();
        }

        CaptchaResultDto captchaDataResult = new CaptchaResultDto();
        String previousCaptchaId = captchaQueryDto.getPreviousCaptchaId();

        switch (captchaQueryDto.getCaptchaType().toUpperCase()) {
            case CaptchaConstants.STANDARD:
                    captchaDataResult = generateTextualCaptchaImage(previousCaptchaId, captchaQueryDto.getLocale(),
                            captchaQueryDto.getCaptchaLength(), captchaQueryDto.isCapitalized());
                    break;
            case CaptchaConstants.WHATS_UP:
                    captchaDataResult = generateWhatsUpCaptchaImage(previousCaptchaId, captchaQueryDto.getDegree());
                    break;
            case CaptchaConstants.SLIDING:
                    captchaDataResult = generateSlidingCaptchaImage(previousCaptchaId, captchaQueryDto.getLocale());
                    break;
        }
        return captchaDataResult;
    }

    /**
     * @param previousCaptchaId the ID of the Captcha
     * @param locale            the chosen locale
     * @return String [] which contains the CaptchaID , Captcha Image , and Captcha Audio.
     */
    public CaptchaResultDto generateTextualCaptchaImage(String previousCaptchaId, Locale locale, Integer captchaLength, boolean capitalized) {

        int extraWidth = (captchaLength != null && captchaLength > CaptchaConstants.DEFAULT_CAPTCHA_LENGTH) ?
                (captchaLength - CaptchaConstants.DEFAULT_CAPTCHA_LENGTH) * CaptchaConstants.DEFAULT_UNIT_WIDTH : 0;

        int extraHeight = (captchaLength != null && captchaLength > CaptchaConstants.DEFAULT_CAPTCHA_LENGTH) ?
                (captchaLength - CaptchaConstants.DEFAULT_CAPTCHA_LENGTH) * CaptchaConstants.DEFAULT_UNIT_HEIGHT : 0;

        System.out.println("extraWidth = " + extraWidth + "extraHeight = " + extraHeight);

        //Case Reload Captcha
        if (previousCaptchaId != null) {
            removeCaptcha(previousCaptchaId);
            counter = 0;
        }
        int captchaTextLength = (captchaLength != null) ? captchaLength : CaptchaConstants.DEFAULT_CAPTCHA_LENGTH;

        Map<String, String> localesMap = new ResourceI18nMapUtil().voiceMap(locale);

        //Generate the Captcha Text
        TextProducer textProducer = new DefaultTextProducer(captchaTextLength, localesMap.keySet());

        //Generate the Captcha drawing
        CaptchaTextRender wordRenderer = new CaptchaTextRender(COLORS, FONTS_SANS_SERIF, FONTS_SERIF);

        //Build The Captcha
        Captcha captcha = Captcha.newBuilder().withDimensions(CAPTCHA_WIDTH + extraWidth, CAPTCHA_HEIGHT + extraHeight).withText(textProducer, wordRenderer, capitalized)
                .withBackground(new GradiatedBackgroundProducer(BACKGROUND_COLORS.get(random.nextInt(BACKGROUND_COLORS.size())),
                        BACKGROUND_COLORS.get(random.nextInt(BACKGROUND_COLORS.size())))).withNoise(new StraightLineImageNoiseProducer(
                        COLOR_STRAIGHT_LINE_NOISE.get(random.nextInt(COLOR_STRAIGHT_LINE_NOISE.size())), 7
                ))
                .gimp(new EuCaptchaGimpyRenderer()).withBorder().build();

        VoiceProducer voiceProducer = new LanguageVoiceProducer(localesMap);

        Double sampleVolume;
        Double noiseVolume;
        if (locale.getLanguage().equals("bg")) {
            sampleVolume = props.getSampleVolume();
            noiseVolume = 0.0D;
        } else {
            sampleVolume = props.getSampleVolume();
            noiseVolume = props.getNoiseVolume();
        }

        //Build the captcha audio file.
        CaptchaAudioService captchaAudioService = CaptchaAudioService.newBuilder()
                .withAnswer(captcha.getAnswer())
                .withVoice(voiceProducer)
                .withNoise(new EuCaptchaNoiseProducer(props.getDefaultNoises(), sampleVolume, noiseVolume))
                .build();
        BufferedImage buf = captcha.getImage();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        String captchaPngImage = "";

        try {
            ImageIO.write(buf, "png", bao);
            bao.flush();
            byte[] imageBytes = bao.toByteArray();
            bao.close();
            captchaPngImage = new String(Base64.getEncoder().encode(imageBytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        InputStream in = captchaAudioService.getChallenge().getAudioInputStream();
        Sample sample = new Sample(in);
        String captchaAudioFile = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            AudioSystem.write(sample.getAudioInputStream(),
                    AudioFileFormat.Type.WAVE, baos);
            byte[] audioBytes = baos.toByteArray();
            baos.close();

            captchaAudioFile = new String(Base64.getEncoder().encode(audioBytes), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String captchaId = this.handleCaptchaId(previousCaptchaId);
        //Adding the Captcha image , the captcha ID , the captcha audio file to the String []

        TextualCaptchaResultDtoDto captchaDataResult = new TextualCaptchaResultDtoDto();
        captchaDataResult.setCaptchaId(captchaId);
        captchaDataResult.setAudioCaptcha(captchaAudioFile);
        captchaDataResult.setCaptchaImg(captchaPngImage);
        captchaDataResult.setCaptchaType(CaptchaConstants.STANDARD);

        addCaptcha(captchaId, captcha.getAnswer());
        log.debug("Generated Captcha with captchaId: {} and answer: {}", captchaId, captcha.getAnswer());
        return captchaDataResult;
    }

    public CaptchaResultDto generateWhatsUpCaptchaImage(String previousCaptchaId, Integer degree) {

        String captchaId = this.handleCaptchaId(previousCaptchaId);
        //Adding the Captcha image , the captcha ID , the captcha audio file to the String []
        Resource resource = captchaWhatsUpImagesService.loadRandomImage();
        if (degree == null) {
            degree = CaptchaConstants.DEFAULT_DEGREE;
        }
        int rotationAngle = degree;
        try {
            rotationAngle = CaptchaRandom.getRandomRotationAngle(degree);
        } catch (WrongCaptchaRotationDegree wrongCaptchaRotationDegree) {
            wrongCaptchaRotationDegree.printStackTrace();
        }

        String captchaPngImage = "";
        WhatsUpCaptchaResultDtoDto captchaDataResult = new WhatsUpCaptchaResultDtoDto();
        try {
            File file = resource.getFile();
            byte[] fileContent = FileUtils.readFileToByteArray(file);
            BufferedImage buffImg = ImageIO.read(file);

            BufferedImage rotatedImage = captchaWhatsUpImagesService.rotate(buffImg, rotationAngle);

            final ByteArrayOutputStream os = new ByteArrayOutputStream();

            try {
                ImageIO.write(rotatedImage, "png", os);
                os.flush();
                byte[] imageBytes = os.toByteArray();
                os.close();
                captchaPngImage = new String(Base64.getEncoder().encode(imageBytes), StandardCharsets.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //	captchaPngImage = encodedString ;

        } catch (IOException e) {
            e.printStackTrace();
        }

        captchaDataResult.setCaptchaType(CaptchaConstants.WHATS_UP);
        captchaDataResult.setCaptchaImg(captchaPngImage);
        captchaDataResult.setCaptchaId(captchaId);
        captchaDataResult.setDegree(degree);

        System.out.println("add to storage captchaId = " + captchaId + " answer = " + rotationAngle);
        addCaptcha(captchaId, Integer.toString(rotationAngle));

        return captchaDataResult;
    }

    public CaptchaResultDto generateSlidingCaptchaImage(String previousCaptchaId, Locale locale) {
        counter = 0;
        String captchaId = this.handleCaptchaId(previousCaptchaId);

        String question = captchaSlidingQuestionService.generateRandomQuestion(locale);
        int[] randomNumbers = captchaSlidingQuestionService.generateRandomNumbers();

        SlidingCaptchaResultDto captchaDataResult = new SlidingCaptchaResultDto();
        captchaDataResult.setCaptchaId(captchaId);
        captchaDataResult.setMax(randomNumbers[1]);
        captchaDataResult.setMin(randomNumbers[0]);
        captchaDataResult.setCaptchaQuestion(formatNumbersIntoString(locale, randomNumbers, question));
        captchaDataResult.setCaptchaType(CaptchaConstants.SLIDING);

        addCaptcha(captchaId, prepareAnswer(captchaSlidingQuestionService.getRandomIndex(), randomNumbers[0], randomNumbers[1]));
        return captchaDataResult;
    }

    public boolean validateCaptcha(String captchaId, String captchaAnswer, String captchaType, boolean usingAudio) {
        if (CaptchaConstants.WHATS_UP.equalsIgnoreCase(captchaType)) {
            return validateWhatsUpCaptcha(captchaId, captchaAnswer);
        } else if (CaptchaConstants.SLIDING.equalsIgnoreCase(captchaType)) {
            return validateSlidingCaptcha(captchaId, captchaAnswer);
        } else
            return validateTextualCaptcha(captchaId, captchaAnswer, usingAudio);
    }

    /**
     * Verify the Captcha based on the CaptchaID stored on the CaptchaCode Map
     *
     * @param captchaId     the ID of the Captcha
     * @param captchaAnswer the users answer on the Captcha
     * @return Boolean of the verification
     */
    public boolean validateTextualCaptcha(String captchaId, String captchaAnswer, boolean usingAudio) {
        boolean result = false;

        if (captchaCodeMap.containsKey(captchaId)) {
            log.debug("Given answer is {}, stored answer is {}", captchaAnswer, captchaCodeMap.get(captchaId));
            //case sensitive
            if (!usingAudio) {
                String answer = StringUtils.deleteWhitespace(captchaCodeMap.get(captchaId));
                result = StringUtils.equals(answer, captchaAnswer);
            }
            //if the audio is selected , ignore case sensitive
            else {
                String answer = StringUtils.deleteWhitespace(captchaCodeMap.get(captchaId));
                result = StringUtils.equalsIgnoreCase(answer, captchaAnswer);
            }
        }
        if(counter == 1) {
            removeCaptcha(captchaId);
            counter = 0;
        } else {
            counter++;
        }
        return result;
    }

    /**
     * Verify the Captcha based on the CaptchaID stored on the CaptchaCode Map
     *
     * @param captchaId     the ID of the Captcha
     * @param captchaAnswer the users answer on the Captcha
     * @return Boolean of the verification
     */
    public boolean validateWhatsUpCaptcha(String captchaId, String captchaAnswer) {
        if (!captchaCodeMap.containsKey(captchaId)) {
            removeCaptcha(captchaId);
            return false;
        }
        String storedAnswer = captchaCodeMap.get(captchaId);
        if(counter == 1) {
            removeCaptcha(captchaId);
            counter = 0;
        } else {
            counter++;
        }
        int storedAnswerAsInt = Integer.parseInt(storedAnswer);
        int givenAnswer = Integer.parseInt(captchaAnswer);

        log.debug("stored answer = , givenAnswer = " + storedAnswerAsInt, givenAnswer);
        return ((givenAnswer == (storedAnswerAsInt * -1)) || ((givenAnswer <= 0) ? ((givenAnswer * -1 - 360) == storedAnswerAsInt) : ((360 - givenAnswer) == storedAnswerAsInt)));
    }

    public boolean validateSlidingCaptcha(String captchaId, String captchaAnswer) {
        if (!captchaCodeMap.containsKey(captchaId)) {
            removeCaptcha(captchaId);
            return false;
        }
        int givenAnswer = Integer.parseInt(captchaAnswer);
        String[] answer = collectAnswer(captchaId);
        int questionNumber = Integer.parseInt(answer[0]);
        int minimumNumber = Integer.parseInt(answer[1]);
        int maximumNumber = Integer.parseInt(answer[2]);

        if(counter == 1) {
            removeCaptcha(captchaId);
        }else {
            counter++;
        }
        if (questionNumber == 1 || questionNumber == 3) {
            return givenAnswer > minimumNumber && givenAnswer < maximumNumber;
        } else if (questionNumber == 2 || questionNumber == 6) {
            return givenAnswer == minimumNumber || givenAnswer == maximumNumber;
        } else if (questionNumber == 4 || questionNumber == 7) {
           return  givenAnswer == maximumNumber;
        } else {
            return givenAnswer == minimumNumber;
        }
    }

    private String prepareAnswer (int questionIndex, int minNumber, int maxNumber) {
        StringBuilder answer = new StringBuilder();
        return answer.append(questionIndex)
                .append(",")
                .append(minNumber)
                .append(",")
                .append(maxNumber)
                .toString();
    }

    private String[] collectAnswer(String captchaId) {
        String answer = captchaCodeMap.get(captchaId);
        return answer.split(",");
    }

    /**
     * @return Captcha ID
     */
    private String handleCaptchaId(String captchaId) {
        if (captchaId != null) {
            removeCaptcha(captchaId);
        }
        return new BigInteger(130, random).toString(32);
    }

    /**
     * Adding the Captcha ID and the answer to the MAP
     *
     * @param captchaId     the ID of the Captcha
     * @param captchaAnswer contains combination of key value
     *                      Captcha ID    =>   Captcha answer
     */
    private static void addCaptcha(String captchaId, String captchaAnswer) {
        captchaCodeMap.putIfAbsent(captchaId, captchaAnswer);
    }

    /**
     * removing the Captcha ID and the answer
     *
     * @param captchaId the ID of the Captcha
     */
    private static void removeCaptcha(String captchaId) {
        captchaCodeMap.remove(captchaId);
    }

    private String formatNumbersIntoString(Locale locale, int[] randomNumbers, String question ) {
        int int_random = random.nextInt(4);
        RuleBasedNumberFormat ruleBasedNumberFormat = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
        if(int_random == 0) {
            String minQuestion = question.replace("{min}", Integer.toString(randomNumbers[0]));
            return minQuestion.replace("{max}", Integer.toString(randomNumbers[1]));
        } else if (int_random == 1) {
            String minQuestion = question.replace("{min}", ruleBasedNumberFormat.format(randomNumbers[0]));
           return  minQuestion.replace("{max}", Integer.toString(randomNumbers[1]));
        } else if (int_random == 2) {
            String maxQuestion = question.replace("{max}", ruleBasedNumberFormat.format(randomNumbers[1]));
            return maxQuestion.replace("{min}", Integer.toString(randomNumbers[0]));
        } else {
            String minQuestion = question.replace("{min}", ruleBasedNumberFormat.format(randomNumbers[0]));
            return minQuestion.replace("{max}", ruleBasedNumberFormat.format(randomNumbers[1]));
        }
    }

}
