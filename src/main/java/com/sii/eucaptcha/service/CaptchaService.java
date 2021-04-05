package com.sii.eucaptcha.service;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import com.sii.eucaptcha.captcha.text.textProducer.impl.DefaultTextProducer;
import com.sii.eucaptcha.captcha.text.image.gimpy.impl.EuCaptchaGimpyRenderer;
import com.sii.eucaptcha.captcha.text.textRender.impl.CaptchaTextRender;
import com.sii.eucaptcha.captcha.audio.voice.impl.LanguageVoiceProducer;
import com.sii.eucaptcha.captcha.util.ResourceI18nMapUtil;
import com.sii.eucaptcha.configuration.properties.SoundConfigProperties;
import com.sii.eucaptcha.controller.constants.CaptchaConstants;
import com.sii.eucaptcha.controller.dto.captcharesult.*;
import com.sii.eucaptcha.controller.dto.captchaquery.CaptchaQueryDto;
import com.sii.eucaptcha.exceptions.CaptchaQueryIsNull;
import com.sii.eucaptcha.exceptions.WrongCaptchaRotationDegree;
import com.sii.eucaptcha.security.CaptchaRandom;
import com.sii.eucaptcha.service.whatsup.CaptchaWhatsUpImagesService;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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

	@Autowired
	SoundConfigProperties props;

	@Autowired
	ResourceLoader resourceLoader ;


	@Autowired
	CaptchaWhatsUpImagesService captchaWhatsUpImagesService ;

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
	public CaptchaResultDto generateCaptchaImage(String previousCaptchaId , Locale locale , Integer captchaLength) {

		int extraWidth = (captchaLength !=null && captchaLength.intValue() >CaptchaConstants.DEFAULT_CAPTCHA_LENGTH) ?
				(captchaLength-CaptchaConstants.DEFAULT_CAPTCHA_LENGTH)*CaptchaConstants.DEFAULT_UNIT_WIDTH : 0 ;

		int extraHeight = (captchaLength !=null && captchaLength.intValue() >CaptchaConstants.DEFAULT_CAPTCHA_LENGTH) ?
				(captchaLength-CaptchaConstants.DEFAULT_CAPTCHA_LENGTH)*CaptchaConstants.DEFAULT_UNIT_HEIGHT : 0 ;

		System.out.println("extraWidth = " + extraWidth + "extraHeight = " + extraHeight);

		//Case Reload Captcha
		if(previousCaptchaId!=null)
			removeCaptcha(previousCaptchaId);
		int captchaTextLength = (captchaLength!=null) ? (int) captchaLength : CaptchaConstants.DEFAULT_CAPTCHA_LENGTH ;

		Map<String, String> localesMap = new ResourceI18nMapUtil().voiceMap(locale);

		//Generate the Captcha Text
		TextProducer textProducer = new DefaultTextProducer(captchaTextLength, localesMap.keySet());

		//Generate the Captcha drawing
		CaptchaTextRender wordRenderer = new CaptchaTextRender(COLORS, FONTS);

		//Build The Captcha
		Captcha captcha = Captcha.newBuilder().withDimensions(CAPTCHA_WIDTH+extraWidth, CAPTCHA_HEIGHT+extraHeight).withText(textProducer ,wordRenderer )
				.withBackground(new GradiatedBackgroundProducer(BACKGROUND_COLORS.get(random.nextInt(BACKGROUND_COLORS.size())),
						BACKGROUND_COLORS.get(random.nextInt(BACKGROUND_COLORS.size())))).withNoise(new StraightLineImageNoiseProducer(
						COLOR_STRAIGHT_LINE_NOISE.get(random.nextInt(COLOR_STRAIGHT_LINE_NOISE.size())),7
				))
				.gimp(new EuCaptchaGimpyRenderer()).withBorder().build();

		VoiceProducer voiceProducer = new LanguageVoiceProducer(localesMap);

		 //Build the captcha audio file.
		CaptchaAudioService captchaAudioService = CaptchaAudioService.newBuilder()
				.withAnswer(captcha.getAnswer())
				.withVoice(voiceProducer)
				.withNoise(new EuCaptchaNoiseProducer(props.getDefaultNoises(), props.getSampleVolume(), props.getNoiseVolume()))
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

		TextualCaptchaResultDtoDto captchaDataResult = new TextualCaptchaResultDtoDto();
		captchaDataResult.setCaptchaId(captchaId);
		captchaDataResult.setAudioCaptcha(captchaAudioFile);
		captchaDataResult.setCaptchaImg(captchaPngImage);
		captchaDataResult.setCaptchaType(CaptchaConstants.STANDARD);

		addCaptcha(captchaId,captcha.getAnswer());

		return captchaDataResult;
	}

	/**
	 * Verify the Captcha based on the CaptchaID stored on the CaptchaCode Map
	 * @param captchaId the ID of the Captcha
	 * @param captchaAnswer the users answer on the Captcha
	 * @return Boolean of the verification
	 */
	public boolean validateCaptcha(String captchaId,String captchaAnswer  ,  boolean usingAudio){
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

	public boolean validateWhatsUpCaptcha(String captchaId , String captchaAnswer ){
		if(!captchaCodeMap.containsKey(captchaId)){
			 removeCaptcha(captchaId);
			 return false;
		}
		String storedAnswer = captchaCodeMap.get(captchaId);
		removeCaptcha(captchaId);
		int sotredAnswerAsInt = Integer.parseInt(storedAnswer);
		int givenAnswer = Integer.parseInt(captchaAnswer);

		System.out.println("stored answer = " + sotredAnswerAsInt + " givenAnswer = " + givenAnswer);

		return ( ( givenAnswer == (sotredAnswerAsInt*-1)) ||  ( (givenAnswer<=0) ? ( ( givenAnswer*-1 - 360 ) == sotredAnswerAsInt ) : ( (360 - givenAnswer ) == sotredAnswerAsInt))  );


	}

	public boolean validateCaptcha(String captchaId,String captchaAnswer , String captchaType ,  boolean usingAudio){
		    if(captchaType != null && CaptchaConstants.WHATS_UP.equalsIgnoreCase(captchaType))
		    	return validateWhatsUpCaptcha(captchaId , captchaAnswer );
		    else
			return validateCaptcha(captchaId , captchaAnswer , usingAudio);
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

	/**
	 * Generate Captcha  Image Wrapper
	 *
	 * @param captchaQueryDto the captcha Query that carry the query parameters
	 * @return response as String contains CaptchaID and Captcha Image
	 */

	public CaptchaResultDto generateCaptchaWrapper(CaptchaQueryDto captchaQueryDto){
		if(captchaQueryDto == null) throw new CaptchaQueryIsNull();

		String previousCaptchaId = captchaQueryDto.getPreviousCaptchaId();
 		if(captchaQueryDto.getCaptchaType() !=null && captchaQueryDto.getCaptchaType().equalsIgnoreCase(CaptchaConstants.WHATS_UP)){
			return generateWhatsUpCaptchaImage(previousCaptchaId , captchaQueryDto.getDegree());
		}else{
			Locale locale = captchaQueryDto.getLocale();
			Integer captchaLength = captchaQueryDto.getCaptchaLength();
			return generateCaptchaImage(previousCaptchaId ,locale , captchaLength );
		}


	}
	public CaptchaResultDto generateCaptchaImage(String previousCaptchaId , Locale locale ){
		return generateCaptchaImage(previousCaptchaId ,locale , null );
	}

	public CaptchaResultDto generateWhatsUpCaptchaImage(String previousCaptchaId , Integer degree){
		if(previousCaptchaId!=null)
			removeCaptcha(previousCaptchaId);
		String captchaId=this.nextCaptchaId();
		//Adding the Captcha image , the captcha ID , the captcha audio file to the String []

		Resource resource = captchaWhatsUpImagesService.loadRandomImage();
		if(degree == null ) degree = CaptchaConstants.DEFAULT_DEGREE;

		int rotationAngle = degree ;
		try {

			 rotationAngle = CaptchaRandom.getRandomRotationAngle(degree);
		} catch (WrongCaptchaRotationDegree wrongCaptchaRotationDegree) {
			wrongCaptchaRotationDegree.printStackTrace();
		}


		String captchaPngImage ="";
		WhatsUpCaptchaResultDtoDto captchaDataResult = new WhatsUpCaptchaResultDtoDto();
		try {
			File file = resource.getFile();
			byte[] fileContent = FileUtils.readFileToByteArray(file);
			BufferedImage buffImg = ImageIO.read(file);

            BufferedImage rotatedImage =   captchaWhatsUpImagesService.rotate(buffImg , rotationAngle);

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
		addCaptcha(captchaId,Integer.toString(rotationAngle));

		return captchaDataResult;
	}







}
