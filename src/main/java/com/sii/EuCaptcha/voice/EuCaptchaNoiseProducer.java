package com.sii.EuCaptcha.voice;

import nl.captcha.audio.Mixer;
import nl.captcha.audio.Sample;
import nl.captcha.audio.noise.NoiseProducer;
import nl.captcha.util.FileUtil;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * @author mousab.aidoud
 * @version 1.0
 */
public class EuCaptchaNoiseProducer implements NoiseProducer {
    private static final Random RAND = new SecureRandom();
    /**
     * List of the audio files for noises
     */
    private static final String[] DEFAULT_NOISES = new String[]{"/sounds/noises/radio_tuning.wav",
            "/sounds/noises/restaurant.wav", "/sounds/noises/swimming.wav"};
    private final String[] _noiseFiles;

    /**
     * Constructor
     */
    public EuCaptchaNoiseProducer() {
        this(DEFAULT_NOISES);
    }

    /**
     * @param noiseFiles
     */
    public EuCaptchaNoiseProducer(String[] noiseFiles) {
        this._noiseFiles = noiseFiles;
    }

    /**
     * Handling the volume of the captcha audio and the volume of the noises
     * @param samples
     * @return audio mixed with noise
     */
    public Sample addNoise(List<Sample> samples) {
        Sample appended = Mixer.append(samples);
        String noiseFile = this._noiseFiles[RAND.nextInt(this._noiseFiles.length)];
        Sample noise = FileUtil.readSample(noiseFile);
        return Mixer.mix(appended, 1.0D, noise, 0.15D);
    }

    /**
     *
     * @return to String
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[Noise files: ");
        sb.append(this._noiseFiles);
        sb.append("]");
        return sb.toString();
    }
}

