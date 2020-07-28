package com.sii.EuCaptcha.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import com.sii.EuCaptcha.text.textRender.EuCaptchaGimpyRender;
import com.sii.EuCaptcha.text.textProducer.*;
import com.sii.EuCaptcha.text.textRender.CaptchaTextRender;
import com.sii.EuCaptcha.voice.CaptchaAudioService;
import com.sii.EuCaptcha.voice.LanguageVoiceProducer;
import com.sii.EuCaptcha.voice.VoiceMap;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpiringMap;
import nl.captcha.Captcha;
import nl.captcha.audio.Sample;
import nl.captcha.audio.producer.VoiceProducer;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.noise.StraightLineNoiseProducer;
import nl.captcha.text.producer.TextProducer;
import org.springframework.stereotype.Service;


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
	private static final int CAPTCHA_WIDTH=400;
	private static final int CAPTCHA_HEIGHT=200;
	private static final long CAPTCHA_EXPIRY_TIME=40;

	/**
	 * List of colors and background colors
	 */
	private static final List<Color> COLORS = new ArrayList<>(3);
	private static final List<Color> COLOR_STRAIGHT_LINE_NOISE = new ArrayList<>(3);
	private static final List<Color> BACKGROUND_COLORS = new ArrayList<>(5);
	private static final List<Color> FISHY_EYE_GIMPY_COLORS = new ArrayList<>(5);

	/**
	 * List of fonts and font sizes
	 */
	private static final List<Font> FONTS = new ArrayList<Font>(3);
	public static String answer = null;

	static {
		COLORS.add(Color.BLACK);
		COLORS.add(Color.GRAY);
		COLORS.add(Color.DARK_GRAY);

		FONTS.add(new Font("Geneva", Font.BOLD, 50));
		FONTS.add(new Font("Courier", Font.BOLD, 70));
		FONTS.add(new Font("Arial", Font.BOLD, 60));

		BACKGROUND_COLORS.add(Color.pink);
		BACKGROUND_COLORS.add(Color.ORANGE);
		BACKGROUND_COLORS.add(Color.lightGray);
		BACKGROUND_COLORS.add(Color.white);
		BACKGROUND_COLORS.add(Color.CYAN);

		COLOR_STRAIGHT_LINE_NOISE.add(Color.red);
		COLOR_STRAIGHT_LINE_NOISE.add(Color.orange);
		COLOR_STRAIGHT_LINE_NOISE.add(Color.magenta);

		FISHY_EYE_GIMPY_COLORS.add(Color.LIGHT_GRAY);
		FISHY_EYE_GIMPY_COLORS.add(Color.lightGray);
	}

	/**
	 * Building a map with Expiration Time CAPTCHA_EXPIRY_TIME
	 */
	private static Map<String, String> captchaCodeMap =
			ExpiringMap.builder().expiration(Long.valueOf(CAPTCHA_EXPIRY_TIME), TimeUnit.SECONDS).build();

	private SecureRandom random = new SecureRandom();
	/**
	 *
	 * @return Captcha ID
	 */
	public String nextCaptchaId() {
		return new BigInteger(130, random).toString(32);
	}
	/**
	 *
	 * @param previousCaptchaId
	 * @param locale
	 * @return String [] which contains the CaptchaID , Captcha Image , and Captcha Audio.
	 */
	public String[] generateCaptchaImage(String previousCaptchaId , Locale locale) {

		/**
		 * Case Reload Captcha
		 */
		if(previousCaptchaId!=null)
			removeCaptcha(previousCaptchaId);

		SecureRandom rand = new SecureRandom();
		BACKGROUND_COLORS.get(rand.nextInt(BACKGROUND_COLORS.size()));

		/**
		 * Generate the Captcha Text
		 */
		TextProducer textProducuer = new LanguageTextProducer().LanguageTextProducer(8,locale);

		/**
		 * Generate the Captcha drawing
		 */
		CaptchaTextRender wordRenderer = new CaptchaTextRender(COLORS, FONTS);

		/**
		 * Build The Captcha
		 */
		Captcha captcha = new Captcha.Builder(CAPTCHA_WIDTH, CAPTCHA_HEIGHT).addText(textProducuer ,wordRenderer )
				.addBackground(new GradiatedBackgroundProducer(BACKGROUND_COLORS.get(rand.nextInt(BACKGROUND_COLORS.size())),
						BACKGROUND_COLORS.get(rand.nextInt(BACKGROUND_COLORS.size())))).addNoise(new StraightLineNoiseProducer(
						COLOR_STRAIGHT_LINE_NOISE.get(rand.nextInt(COLOR_STRAIGHT_LINE_NOISE.size())),7
				))
				.gimp(new EuCaptchaGimpyRender()).addBorder().build();
		/**
		 * Adding the voice map for the selected language
		 */
		Map<String, String> voicesMap = new HashMap<String, String>();
	    voicesMap = new VoiceMap().mapVoiceLettresAndNumbersEN(locale);

		VoiceProducer vProd = new LanguageVoiceProducer(captcha.getAnswer() , voicesMap);
        CaptchaAudioService captchaAudioService = null;
		/**
		 * Build the captcha audio file.
		 */
		if 	(locale.getLanguage() == "bg") {
		captchaAudioService = new CaptchaAudioService.Builder()
				.addAnswer(captcha.getAnswer())
				.addVoice(vProd)
				.build();
		}else{
			captchaAudioService = new CaptchaAudioService.Builder()
					.addAnswer(captcha.getAnswer())
					.addVoice(vProd)
					.addNoise()
					.build();
		}
		BufferedImage buf = captcha.getImage();
		ByteArrayOutputStream bao = new ByteArrayOutputStream();

		String captchaPngImage = null;

		try {
			ImageIO.write(buf, "png", bao);
			bao.flush();
			byte[] imageBytes = bao.toByteArray();
			bao.close();
			captchaPngImage = new String(Base64.getEncoder().encode(imageBytes), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		InputStream in = captchaAudioService.getChallenge().getAudioInputStream();
		Sample sample = new Sample(in);
		String captchaAudioFile = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			AudioSystem.write(sample.getAudioInputStream(),
					AudioFileFormat.Type.WAVE, baos );
			byte[] audioBytes =  baos.toByteArray();
			baos.close();

			captchaAudioFile = new String(Base64.getEncoder().encode(audioBytes), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String captchaId=this.nextCaptchaId();

		/**
		 * Adding the Captcha image , the captcha ID , the captcha audio file to the String []
		 */
		String[] imageParams = {captchaPngImage,captchaId,captchaAudioFile };
		addCaptcha(captchaId,captcha.getAnswer());

		return imageParams;
	}

	/**
	 * Verify the Captcha based on the CaptchaID stored on the CaptchaCode Map
	 * @param captchaId
	 * @param captchaAnswer
	 * @return Boolean of the verification
	 */
	public boolean validateCaptcha(String captchaId,String captchaAnswer , boolean usingAudio){
		boolean result=false;
		/**
		 * case sensitive
		 */
		if (!usingAudio) {
			if (captchaCodeMap.containsKey(captchaId) && captchaCodeMap.get(captchaId).equals(captchaAnswer))
				result = true;
		}
		/**
		 * if the audio is selected , ignore case sensitive
		 */
		else
		{
			if (captchaCodeMap.containsKey(captchaId) && captchaCodeMap.get(captchaId).equalsIgnoreCase(captchaAnswer))
				result = true;
		}
		removeCaptcha(captchaId);
		return result;
	}

	/**
	 * Adding the Captcha ID and the answer the the MAP
	 * @param captchaId
	 * @param captchaAnswer contains combination of key value
	 *                    Captcha ID    =>   Captcha answer
	 */

	private  static void addCaptcha(String captchaId,String captchaAnswer) {
		captchaCodeMap.putIfAbsent(captchaId, captchaAnswer);
	}

	/**
	 * removing the Captcha ID and it and answer
	 * @param captchaId
	 */
	private static void removeCaptcha(String captchaId){
		if(captchaCodeMap.containsKey(captchaId)){
			captchaCodeMap.remove(captchaId);
		}
	}

}
