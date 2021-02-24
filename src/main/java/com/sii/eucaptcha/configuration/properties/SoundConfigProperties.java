package com.sii.eucaptcha.configuration.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:sound.properties")
@ConfigurationProperties(prefix = "sound.noises")
public class SoundConfigProperties {

    @Value("${sound.noises.default}")
    private String[] defaultNoises;

    public String[] getDefaultNoises() {
        return defaultNoises;
    }

    public void setDefaultNoises(String[] defaultNoises) {
        this.defaultNoises = defaultNoises;
    }
}
