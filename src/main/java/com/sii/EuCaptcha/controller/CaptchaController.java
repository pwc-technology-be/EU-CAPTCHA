package com.sii.EuCaptcha.controller;

import com.sii.EuCaptcha.security.JwtToken;
import com.sii.EuCaptcha.service.CaptchaService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.JsonObject;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
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

    @Autowired
    CaptchaService service;

    @Autowired
    JwtToken jwtToken;

    /**
     * Captcha answer length -> application.properties
     */
    @Value("${captcha.answer.length}")
    private int captchaAnswerLength;
    /**
     * Captcha ID Length  -> application.properties
     */
    @Value("${captcha.id.length}")
    private int captchaIdLength;


    /**
     * Get the Captcha (Id + Captcha Image + Captcha Audio)
     * @param locale
     * @return response as String contains CaptchaID and Captcha Image
     */

    @GetMapping(value = "/captchaImg")
    public ResponseEntity<String> getCaptchaImage(@ApiParam(hidden = true) Locale locale) {
        String[] captchaData = service.generateCaptchaImage(null, locale);
        JsonObject response = new JsonObject();
        /**
         * Adding data to the Jason Object().
         */
        response.addProperty("captchaId", captchaData[1]);
        response.addProperty("captchaImg", captchaData[0]);
        response.addProperty("audioCaptcha", captchaData[2]);
        /**
         * Adding the token to the Http Header
         */
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");

        byte[] decodedKey = Base64.getDecoder().decode(captchaData[1]);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        headers.add("token", jwtToken.generateJwtToken(originalKey));
        return new ResponseEntity<String>(response.toString(), headers, HttpStatus.OK);
    }

    /**
     * Reloading the captcha Image
     * @param previousCaptchaId
     * @param locale
     * @return response as String contains CaptchaID and Captcha Image
     */

    @GetMapping(value = "/reloadCaptchaImg/{previousCaptchaId}")
    public ResponseEntity<String> reloadCaptchaImage(@PathVariable("previousCaptchaId") String previousCaptchaId,
                                                     Locale locale,
                                                     @RequestHeader("jwtString") String jwtString) {




        try {
            byte[] decodedKey = Base64.getDecoder().decode(previousCaptchaId);
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            // Reload the captcha if the token is valid
            if (jwtToken.verifyToken(jwtString , originalKey)) {
                String[] captchaData = service.generateCaptchaImage(previousCaptchaId, locale);
                JsonObject response = new JsonObject();
                response.addProperty("captchaId", captchaData[1]);
                response.addProperty("captchaImg", captchaData[0]);
                response.addProperty("audioCaptcha", captchaData[2]);
                return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("You can't get access ", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("sorry , you can't get access", HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Validating the captcha answer :
     * @param captchaId
     * @param captchaAnswer
     * @return fail or success as String response
     */

    @PostMapping(value = "/validateCaptcha/{captchaId}")
    public ResponseEntity<String> validateCaptcha(@PathVariable("captchaId") String captchaId,
                                                  @RequestParam(value = "captchaAnswer", required = false) String captchaAnswer,
                                                  @RequestParam(value = "useAudio", required = false) boolean useAudio,
                                                  @RequestHeader("jwtString") String jwtString) {

        /**
         * Verify the validity of the captcha answer.
         */
        if (captchaAnswer.trim().length() != captchaAnswerLength ||
                captchaId.trim().length() != captchaIdLength
                || jwtString == null) {
            return new ResponseEntity<String>("Sorry, you should give a correct answer", HttpStatus.NOT_ACCEPTABLE);
        }else {

            byte[] decodedKey = Base64.getDecoder().decode(captchaId);
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        try {
            if (jwtToken.verifyToken(jwtString , originalKey)) {
                boolean responseCaptcha = false;
                /**
                 * the Token is valid , we proceed the validation of the captcha
                 */
                responseCaptcha = service.validateCaptcha(captchaId, captchaAnswer, useAudio);
                JsonObject response = new JsonObject();
                /**
                 * response captcha ( valid -> success || invalid -> fail  )
                 */
                response.addProperty("responseCaptcha", responseCaptcha ? "success" : "fail");
                return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
            } else {
                /**
                 * The token is not valid .
                 */
                return new ResponseEntity<String>("Sorry, you can't get access ", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("Sorry, you can't get access", HttpStatus.FORBIDDEN);
        }}
    }
}