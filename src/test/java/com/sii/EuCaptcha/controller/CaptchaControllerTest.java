package com.sii.EuCaptcha.controller;

import com.sii.EuCaptcha.service.CaptchaService;
import net.jodah.expiringmap.ExpiringMap;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CaptchaController.class)
public class CaptchaControllerTest {
/*
    @Autowired
    private MockMvc mvc;

    @Mock
    private CaptchaService captchaService;


    @Before
    public void init(){

        String[] resultGeneratedCaptcah = new String[0];
        resultGeneratedCaptcah[0] = "captchaId " ;
        resultGeneratedCaptcah[1] = "Image" ;
        when(this.captchaService.generateCaptchaImage(any(), any())).thenReturn(resultGeneratedCaptcah);
    }

    @DisplayName("Test geting Captcha ")
    @Test
    public void getCaptcha() throws Exception {

        this.mvc.perform( MockMvcRequestBuilders
                .get("/api/captchaImg")
                .accept(MediaType.ALL_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

    }

    @DisplayName("Test reloading Captcha ")
    @Test
    public void reloadCaptchaImage() throws Exception {

        this.mvc.perform( MockMvcRequestBuilders
                .get("/api/reloadCaptchaImg/{previousCaptchaId}" , "jjq7u4reu1vaiuao28gjq4vkq4")
                .accept(MediaType.ALL_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

    }

    @DisplayName("Test reloading Captcha without previous CaptchaID ")
    @Test
    public void reloadCaptchaImage_without_previous_CaptchaID() throws Exception {

        this.mvc.perform( MockMvcRequestBuilders
                .get("/api/reloadCaptchaImg/" )
                .accept(MediaType.ALL_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Test reloading Captcha with invalid previous CaptchaID ")
    @Test
    public void reloadCaptchaImage_with_Invalid_previous_capthcaId() throws Exception {

        String previousCaptchaId = "l1qgsp6";
        this.mvc.perform( MockMvcRequestBuilders
                .get("/api/reloadCaptchaImg/{previousCaptchaId}" , previousCaptchaId)
                .accept(MediaType.ALL_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());

    }

    @DisplayName("Test validating Captcha with get function instead of post ")
    @Test
    public void validate_Captcha_with_get_function_and_not_post() throws Exception {

        String CaptchaId = "l1qgsp6";
        this.mvc.perform( MockMvcRequestBuilders
                .get("/api/validateCaptcha/{previousCaptchaId}" , CaptchaId)
                .accept(MediaType.ALL_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isMethodNotAllowed());
    }

    @DisplayName("Test validating Captcha with get null answer ")
    @Test
    public void validate_Captcha_with_answer_null() throws Exception {

        String CaptchaId = "l1qgsp6";
        this.mvc.perform( MockMvcRequestBuilders
                .post("/api/validateCaptcha/{previousCaptchaId}" , CaptchaId)
                .accept(MediaType.ALL_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotAcceptable());
    }

    @DisplayName("Test validating Captcha with valid answer and invalid CaptchaID")
    @Test
    public void validate_Captcha_with_valid_answer_and_invalid_CaptchaID() throws Exception {

        String CaptchaId = "l1qgsp6";
        String answerCaptcha = "LKH1D25D";
        this.mvc.perform( MockMvcRequestBuilders
                .post("/api/validateCaptcha/{previousCaptchaId}" , CaptchaId)
                .param("captchaAnswer", answerCaptcha)
                .accept(MediaType.ALL_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Test validating Captcha with invalid answer and valid CaptchaID")
    @Test
    public void validate_Captcha_with_invalid_answer_and_valid_CaptchaID() throws Exception {

        String CaptchaId = "5m8v8tupqd0hqkjj1nrl1qgsp6";
        String answerCaptcha = "LKH1";
        this.mvc.perform( MockMvcRequestBuilders
                .post("/api/validateCaptcha/{previousCaptchaId}" , CaptchaId)
                .param("captchaAnswer", answerCaptcha)
                .accept(MediaType.ALL_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotAcceptable());

    }

    @DisplayName("Test validating Captcha with valid answer and valid CaptchaID")
    @Test
    public void validate_Captcha_with_valid_answer_and_valid_CaptchaID() throws Exception {

        String CaptchaId = "5m8v8tupqd0hqkjj1nrl1qgsp6";
        String answerCaptcha = "LKH14521";
        this.mvc.perform( MockMvcRequestBuilders
                .post("/api/validateCaptcha/{previousCaptchaId}" , CaptchaId)
                .param("captchaAnswer", answerCaptcha)
                .accept(MediaType.ALL_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

*/
}
