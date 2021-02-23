package com.sii.eucaptcha.captcha.audio.noise.impl;

import com.sii.eucaptcha.captcha.audio.Mixer;
import com.sii.eucaptcha.captcha.audio.Sample;
import com.sii.eucaptcha.captcha.audio.noise.NoiseProducer;
import com.sii.eucaptcha.captcha.util.FileUtil;
import com.sii.eucaptcha.security.CaptchaRandom;
import org.springframework.beans.factory.annotation.Value;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * @author mousab.aidoud
 * @version 1.0
 */
public class EuCaptchaNoiseProducer implements NoiseProducer {

    private static final SecureRandom RANDOM = CaptchaRandom.getSecureInstance();

    /**
     * List of the audio files for noises
     */
    private static final String[] DEFAULT_NOISES = new String[]{"/sounds/noises/radio_tuning.wav",
            "/sounds/noises/restaurant.wav", "/sounds/noises/swimming.wav", "/sounds/noises/zombie.wav"};

    @Value("${captcha.audio.noises}")
    private final String[] noiseFiles;

    /**
     * Constructor
     */
    public EuCaptchaNoiseProducer() {
        this(DEFAULT_NOISES);
    }

    /**
     * @param noiseFiles the noises to be mixed in the background of the spoken Captcha
     */
    public EuCaptchaNoiseProducer(String[] noiseFiles) {
        this.noiseFiles = noiseFiles;
    }

    /**
     * Handling the volume of the captcha audio and the volume of the noises
     * @param noiseSamples the audio files to generate the spoken Captcha
     * @return audio mixed with noise
     */
    @Override
    public Sample produceNoise(List<Sample> noiseSamples) {
        Sample appended = Mixer.append(noiseSamples);
        String noiseFile = this.noiseFiles[RANDOM.nextInt(this.noiseFiles.length)];
        Sample noise = FileUtil.readSample(noiseFile);
        return Mixer.mix(appended, 1.0D, noise, 0.15D);
    }

    /**
     * @return to String
     */
    public String toString() {
        return "[Noise files: " +
                Arrays.toString(this.noiseFiles) +
                "]";
    }
}

