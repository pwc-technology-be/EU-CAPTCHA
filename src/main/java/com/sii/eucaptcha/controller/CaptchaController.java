package com.sii.eucaptcha.controller;

import com.google.gson.JsonObject;
import com.sii.eucaptcha.security.JwtToken;
import com.sii.eucaptcha.service.CaptchaService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

/**
 * @author mousab.aidoud
 * Captcha Rest Controller class with those methodes : getCaptchaImage , reloadCaptchaImage , validateCaptcha.
 */
@RestController
@RequestMapping("/api")
@Slf4j
@CrossOrigin("*")
public class CaptchaController {

    private final CaptchaService captchaService;
    private final JwtToken jwtToken;

    /**
     * Captcha answer length -> controller.properties
     */
    @Value("${controller.captcha.answerLength}")
    private int captchaAnswerLength;
    /**
     * Captcha ID Length  -> controller.properties
     */
    @Value("${controller.captcha.idLength}")
    private int captchaIdLength;

    public CaptchaController(CaptchaService captchaService, JwtToken jwtToken) {
        this.captchaService = captchaService;
        this.jwtToken = jwtToken;
    }


    /**
     * Get the Captcha (Id + Captcha Image + Captcha Audio)
     *
     * @param locale the chosen locale
     * @return response as String contains CaptchaID and Captcha Image
     */
    @GetMapping(value = "/captchaImg")
    public ResponseEntity<String> getCaptchaImage(@ApiParam(hidden = true) Locale locale) {
        return createResponse(captchaService.generateCaptchaImage(null, locale));
    }

    /**
     * Reloading the captcha Image
     *
     * @param previousCaptchaId the ID of the previous Captcha
     * @param locale            the chosen Locale
     * @return response as String contains CaptchaID and Captcha Image
     */
    @GetMapping(value = "/reloadCaptchaImg/{previousCaptchaId}")
    public ResponseEntity<String> reloadCaptchaImage(@PathVariable("previousCaptchaId") String previousCaptchaId,
                                                     Locale locale,
                                                     @RequestHeader("jwtString") String jwtString) {

        try {
            // Reload the captcha if the token is valid
            if (jwtToken.verifyToken(jwtString)) {
                return createResponse(captchaService.generateCaptchaImage(previousCaptchaId, locale));
            } else {
                return new ResponseEntity<>("You can't get access ", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("sorry , you can't get access", HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Validating the captcha answer :
     *
     * @param captchaId     the ID of the Captcha
     * @param captchaAnswer the answer of the Captcha -> success or fail
     * @return fail or success as String response
     */
    @PostMapping(value = "/validateCaptcha/{captchaId}")
    public ResponseEntity<String> validateCaptcha(@PathVariable("captchaId") String captchaId,
                                                  @RequestParam(value = "captchaAnswer", required = false) String captchaAnswer,
                                                  @RequestParam(value = "useAudio", required = false) boolean useAudio,
                                                  @RequestHeader("jwtString") String jwtString) {

        //Verify the validity of the captcha answer.
        if (captchaAnswer.trim().length() != captchaAnswerLength ||
                captchaId.trim().length() != captchaIdLength
                || jwtString == null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            try {
                if (jwtToken.verifyToken(jwtString)) {
                    boolean responseCaptcha;
                    //the Token is valid , we proceed the validation of the captcha
                    responseCaptcha = captchaService.validateCaptcha(captchaId, captchaAnswer, useAudio);
                    JsonObject response = new JsonObject();
                    //response captcha ( valid -> success || invalid -> fail  )
                    response.addProperty("responseCaptcha", responseCaptcha ? "success" : "fail");
                    return new ResponseEntity<>(response.toString(), HttpStatus.OK);
                } else {
                    //The token is not valid.
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
    }

    private ResponseEntity<String> createResponse(String[] captchaData) {
        JsonObject response = new JsonObject();
        //Adding data to the Jason Object().
        response.addProperty("captchaId", captchaData[1]);
        response.addProperty("captchaImg", captchaData[0]);
        response.addProperty("audioCaptcha", captchaData[2]);
        //Adding the token to the Http Header
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,"jwtString");
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "jwtString");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("jwtString", jwtToken.generateJwtToken());
        return new ResponseEntity<>(response.toString(), headers, HttpStatus.OK);
    }
}