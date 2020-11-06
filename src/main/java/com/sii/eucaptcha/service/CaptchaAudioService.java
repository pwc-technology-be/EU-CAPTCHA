package com.sii.eucaptcha.service;
import com.sii.eucaptcha.captcha.audio.Mixer;
import com.sii.eucaptcha.captcha.audio.Sample;
import com.sii.eucaptcha.captcha.audio.noise.NoiseProducer;
import com.sii.eucaptcha.captcha.audio.noise.impl.EuCaptchaNoiseProducer;
import com.sii.eucaptcha.captcha.audio.voice.VoiceProducer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author mousab.aidoud
 * @version 1.0
 * Captcha Audio class
 */
public class CaptchaAudioService {

    private static final Random SECURE_RANDOM = new SecureRandom();

    private final CaptchaAudioService.Builder builder;

    CaptchaAudioService(CaptchaAudioService.Builder builder) {
        this.builder = builder;
    }

    public static class Builder {

        private String answer = "";
        private Sample challenge;
        private final List<VoiceProducer> voiceProducers;
        private final List<NoiseProducer> noiseProducers;

        public Builder() {
            voiceProducers = new ArrayList<>();
            noiseProducers = new ArrayList<>();
        }


        public CaptchaAudioService.Builder addAnswer(String ansProd) {
            answer += ansProd;

            return this;
        }

        public CaptchaAudioService.Builder addVoice(VoiceProducer vProd) {
            voiceProducers.add(vProd);

            return this;
        }

        public CaptchaAudioService.Builder addNoise() {
            return addNoise(new EuCaptchaNoiseProducer());
        }

        public CaptchaAudioService.Builder addNoise(NoiseProducer noiseProd) {
            noiseProducers.add(noiseProd);

            return this;
        }

        /**
         * Build the audio
         * @return captcha audio service builder
         */
        public CaptchaAudioService build() {



            // Convert answer to an array
            char[] ansAry = answer.toCharArray();

            // Make a List of Samples for each character
            VoiceProducer vProd;
            List<Sample> samples = new ArrayList<>();
            Sample sample;
            for (char c : ansAry) {
                // Create Sample for this character from one of the
                // VoiceProducers
                vProd = voiceProducers.get(SECURE_RANDOM.nextInt(voiceProducers.size()));
                sample = vProd.getVocalization(c);
                samples.add(sample);
            }
            // 3. Add noise, if any, and return the result
            if (noiseProducers.size() > 0) {
                NoiseProducer nProd = noiseProducers.get(SECURE_RANDOM.nextInt(noiseProducers
                        .size()));
                challenge = nProd.addNoise(samples);
                return new CaptchaAudioService(this);
            }
            challenge = Mixer.append(samples);
            return new CaptchaAudioService(this);
        }

        /**
         *
         * @return answer
         */
        @Override public String toString() {

            return "[Answer: " +
                    answer +
                    "]";
        }
    }


    /**
     *
     * @return answer builder
     */
    public String getAnswer() {
        return builder.answer;
    }

    /**
     *
     * @return challenge
     */
    public Sample getChallenge() {
        return builder.challenge;
    }

    /**
     *
     * @return builder to string
     */
    @Override public String toString() {
        return builder.toString();
    }
}
