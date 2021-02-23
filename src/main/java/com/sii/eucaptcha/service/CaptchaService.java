package com.sii.eucaptcha.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import com.sii.eucaptcha.captcha.Captcha;
import com.sii.eucaptcha.captcha.audio.Sample;
import com.sii.eucaptcha.captcha.audio.noise.impl.EuCaptchaNoiseProducer;
import com.sii.eucaptcha.captcha.audio.voice.VoiceProducer;
import com.sii.eucaptcha.captcha.text.image.background.impl.GradiatedBackgroundProducer;
import com.sii.eucaptcha.captcha.text.image.noise.impl.StraightLineImageNoiseProducer;
import com.sii.eucaptcha.captcha.text.textProducer.TextProducer;
import com.sii.eucaptcha.captcha.text.textProducer.impl.LanguageTextProducer;
import com.sii.eucaptcha.captcha.text.image.gimpy.impl.EuCaptchaGimpyRenderer;
import com.sii.eucaptcha.captcha.text.textRender.impl.CaptchaTextRender;
import com.sii.eucaptcha.captcha.audio.voice.impl.LanguageVoiceProducer;
import com.sii.eucaptcha.captcha.audio.voice.impl.VoiceMap;
import com.sii.eucaptcha.security.CaptchaRandom;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpiringMap;
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

	/**
	 * List of fonts and font sizes
	 */
	private static final List<Font> FONTS = new ArrayList<>(3);

	static {
		COLORS.add(Color.BLACK);
		COLORS.add(Color.GRAY);
		COLORS.add(Color.DARK_GRAY);

		FONTS.add(new Font("Geneva", Font.BOLD, 50));
		FONTS.add(new Font("Courier", Font.BOLD, 70));
		FONTS.add(new Font("Arial", Font.BOLD, 60));

		BACKGROUND_COLORS.add(Color.PINK);
		BACKGROUND_COLORS.add(Color.ORANGE);
		BACKGROUND_COLORS.add(Color.LIGHT_GRAY);
		BACKGROUND_COLORS.add(Color.WHITE);
		BACKGROUND_COLORS.add(Color.CYAN);

		COLOR_STRAIGHT_LINE_NOISE.add(Color.RED);
		COLOR_STRAIGHT_LINE_NOISE.add(Color.ORANGE);
		COLOR_STRAIGHT_LINE_NOISE.add(Color.MAGENTA);

	}

	/**
	 * Building a map with Expiration Time CAPTCHA_EXPIRY_TIME
	 */
	private static final Map<String, String> captchaCodeMap =
			ExpiringMap.builder().expiration(CAPTCHA_EXPIRY_TIME, TimeUnit.SECONDS).build();

	private final SecureRandom random = CaptchaRandom.getSecureInstance();
	/**
	 *
	 * @return Captcha ID
	 */
	public String nextCaptchaId() {
		return new BigInteger(130, random).toString(32);
	}
	/**
	 *
	 * @param previousCaptchaId the ID of the Captcha
	 * @param locale the chosen locale
	 * @return String [] which contains the CaptchaID , Captcha Image , and Captcha Audio.
	 */
	public String[] generateCaptchaImage(String previousCaptchaId , Locale locale) {

		//Case Reload Captcha
		if(previousCaptchaId!=null)
			removeCaptcha(previousCaptchaId);

		SecureRandom rand = CaptchaRandom.getSecureInstance();

		//Generate the Captcha Text
		TextProducer textProducer = new LanguageTextProducer().getLanguageTextProducer(8,locale);

		//Generate the Captcha drawing
		CaptchaTextRender wordRenderer = new CaptchaTextRender(COLORS, FONTS);

		//Build The Captcha
		Captcha captcha = Captcha.newBuilder().withDimensions(CAPTCHA_WIDTH, CAPTCHA_HEIGHT).withText(textProducer ,wordRenderer )
				.withBackground(new GradiatedBackgroundProducer(BACKGROUND_COLORS.get(rand.nextInt(BACKGROUND_COLORS.size())),
						BACKGROUND_COLORS.get(rand.nextInt(BACKGROUND_COLORS.size())))).withNoise(new StraightLineImageNoiseProducer(
						COLOR_STRAIGHT_LINE_NOISE.get(rand.nextInt(COLOR_STRAIGHT_LINE_NOISE.size())),7
				))
				.gimp(new EuCaptchaGimpyRenderer()).withBorder().build();
		//Adding the voice map for the selected language
		Map<String, String> voicesMap;
	    voicesMap = new VoiceMap().voiceMapLettersAndNumbers(locale);

		VoiceProducer voiceProducer = new LanguageVoiceProducer(voicesMap);

		 //Build the captcha audio file.
		CaptchaAudioService captchaAudioService = CaptchaAudioService.newBuilder()
				.withAnswer(captcha.getAnswer())
				.withVoice(voiceProducer)
				.withNoise(new EuCaptchaNoiseProducer())
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
					AudioFileFormat.Type.WAVE, baos );
			byte[] audioBytes =  baos.toByteArray();
			baos.close();

			captchaAudioFile = new String(Base64.getEncoder().encode(audioBytes), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String captchaId=this.nextCaptchaId();
		//Adding the Captcha image , the captcha ID , the captcha audio file to the String []
		String[] imageParams = {captchaPngImage,captchaId,captchaAudioFile };
		addCaptcha(captchaId,captcha.getAnswer());

		return imageParams;
	}

	/**
	 * Verify the Captcha based on the CaptchaID stored on the CaptchaCode Map
	 * @param captchaId the ID of the Captcha
	 * @param captchaAnswer the users answer on the Captcha
	 * @return Boolean of the verification
	 */
	public boolean validateCaptcha(String captchaId,String captchaAnswer , boolean usingAudio){
		boolean result=false;

		//case sensitive
		if (!usingAudio) {
			if (captchaCodeMap.containsKey(captchaId) && captchaCodeMap.get(captchaId).equals(captchaAnswer))
				result = true;
		}
		//if the audio is selected , ignore case sensitive
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
	 * @param captchaId the ID of the Captcha
	 * @param captchaAnswer contains combination of key value
	 *                    Captcha ID    =>   Captcha answer
	 */

	private  static void addCaptcha(String captchaId,String captchaAnswer) {
		captchaCodeMap.putIfAbsent(captchaId, captchaAnswer);
	}

	/**
	 * removing the Captcha ID and it and answer
	 * @param captchaId the ID of the Captcha
	 */
	private static void removeCaptcha(String captchaId){
		captchaCodeMap.remove(captchaId);
	}

}
