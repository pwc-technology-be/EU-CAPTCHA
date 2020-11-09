package com.sii.eucaptcha.captcha.audio.noise;


import com.sii.eucaptcha.captcha.audio.Sample;

import java.util.List;

public interface NoiseProducer {
    Sample addNoise(List<Sample> target);
}
