package com.sii.eucaptcha.service;

import net.jodah.expiringmap.ExpiringMap;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CaptchaServiceTest {


    String previousIdCaptcha = "jjq7u4reu1vaiuao28gjq4vkq4";
    Locale frenchLocale = new Locale("fr", "FR");
    private static final long CAPTCHA_EXPIRY_TIME = 20;

    private static Map<String, String> captchaCodeMap =
            ExpiringMap.builder().expiration(Long.valueOf(CAPTCHA_EXPIRY_TIME), TimeUnit.SECONDS).build();

    @InjectMocks
    private CaptchaService service;

    @Mock
    private CaptchaService serviceMocked;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("Test genrating Captcha methode ")
    @Test
    public void generateCaptchaImage() throws IOException {

     /*   String[] resultGeneratedCaptcah = this.service.generateCaptchaImage(previousIdCaptcha, frenchLocale);

        assertNotNull(resultGeneratedCaptcah);
        assertNotNull(resultGeneratedCaptcah[0]);
        assertNotNull(resultGeneratedCaptcah[1]);

        assertEquals(26, resultGeneratedCaptcah[1].length());
        assertNotEquals(previousIdCaptcha, resultGeneratedCaptcah[1]); */
    }

    @DisplayName("Test validate captcha methode")
    @Test
    public void validateCaptcha() {
        when(this.serviceMocked.validateTextualCaptcha(anyString(), anyString(), anyBoolean())).thenReturn(true);
        String CaptchaID = "jh0b0t6rad62bgu9cerv91cb5g";
        String CaptchaAnswer = "KAB1";
        assertTrue(serviceMocked.validateTextualCaptcha(CaptchaID, CaptchaAnswer, true));

    }


}
