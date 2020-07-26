package com.sii.EuCaptcha.voice;
import nl.captcha.audio.Mixer;
import nl.captcha.audio.Sample;
import nl.captcha.audio.noise.NoiseProducer;
import nl.captcha.audio.producer.VoiceProducer;
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

    public static final String NAME = "captchaAudioService";
    private static final Random RAND = new SecureRandom();

    private CaptchaAudioService.Builder _builder;

    CaptchaAudioService(CaptchaAudioService.Builder builder) {
        _builder = builder;
    }

    public static class Builder {

        private String _answer = "";
        private Sample _challenge;
        private List<VoiceProducer> _voiceProds;
        private List<NoiseProducer> _noiseProds;

        public Builder() {
            _voiceProds = new ArrayList<VoiceProducer>();
            _noiseProds = new ArrayList<NoiseProducer>();
        }


        public CaptchaAudioService.Builder addAnswer(String ansProd) {
            _answer += ansProd;

            return this;
        }

        public CaptchaAudioService.Builder addVoice(VoiceProducer vProd) {
            _voiceProds.add(vProd);

            return this;
        }

        public CaptchaAudioService.Builder addNoise() {
            return addNoise(new EuCaptchaNoiseProducer());
        }

        public CaptchaAudioService.Builder addNoise(NoiseProducer noiseProd) {
            _noiseProds.add(noiseProd);

            return this;
        }

        /**
         * Build the audio
         * @return captcha audio service builder
         */
        public CaptchaAudioService build() {



            // Convert answer to an array
            char[] ansAry = _answer.toCharArray();

            // Make a List of Samples for each character
            VoiceProducer vProd;
            List<Sample> samples = new ArrayList<Sample>();
            Sample sample;
            for (int i = 0; i < ansAry.length; i++) {
                // Create Sample for this character from one of the
                // VoiceProducers
                vProd = _voiceProds.get(RAND.nextInt(_voiceProds.size()));
                sample = vProd.getVocalization(ansAry[i]);
                samples.add(sample);
            }
            // 3. Add noise, if any, and return the result
            if (_noiseProds.size() > 0) {
                NoiseProducer nProd = _noiseProds.get(RAND.nextInt(_noiseProds
                        .size()));
                _challenge = nProd.addNoise(samples);
                return new CaptchaAudioService(this);
            }
            _challenge = Mixer.append(samples);
            return new CaptchaAudioService(this);
        }

        /**
         *
         * @return answer
         */
        @Override public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("[Answer: ");
            sb.append(_answer);
            sb.append("]");

            return sb.toString();
        }
    }

    /**
     *
     * @param answer
     * @return
     */
    public boolean isCorrect(String answer) {
        return answer.equals(_builder._answer);
    }

    /**
     *
     * @return answer builder
     */
    public String getAnswer() {
        return _builder._answer;
    }

    /**
     *
     * @return challenge
     */
    public Sample getChallenge() {
        return _builder._challenge;
    }

    /**
     *
     * @return builder to string
     */
    @Override public String toString() {
        return _builder.toString();
    }
}
