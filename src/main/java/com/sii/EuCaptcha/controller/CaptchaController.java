package com.sii.EuCaptcha.controller;

import com.sii.EuCaptcha.security.JwtToken;
import com.sii.EuCaptcha.service.CaptchaService;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.JsonObject;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Locale;


@RestController
@RequestMapping("/api")
@Slf4j
@CrossOrigin("*")
public class CaptchaController {

    @Autowired
    CaptchaService service;

    @Autowired
    JwtToken jwtToken;


    private Key key;

    /**
     * @param locale
     * @return response as String contains CaptchaID and Captcha Image
     */

    @GetMapping(value = "/captchaImg")
    public ResponseEntity<String> getCaptchaImage(@ApiParam(hidden = true) Locale locale) {
        String[] captchaData = service.generateCaptchaImage(null, locale);
        JsonObject response = new JsonObject();
        response.addProperty("captchaId", captchaData[1]);
        response.addProperty("captchaImg", captchaData[0]);
        response.addProperty("audioCaptcha", captchaData[2]);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        SecureRandom rand = new SecureRandom();
        key = MacProvider.generateKey(SignatureAlgorithm.HS512 , rand);
        headers.add("token", jwtToken.generateJwtToken(key));
        return new ResponseEntity<String>(response.toString(), headers, HttpStatus.OK);
    }

    /**
     * Reloading the captcha Image
     *
     * @param previousCaptchaId
     * @param locale
     * @return response as String contains CaptchaID and Captcha Image
     */

    @GetMapping(value = "/reloadCaptchaImg/{previousCaptchaId}")
    public ResponseEntity<String> reloadCaptchaImage(@PathVariable("previousCaptchaId") String previousCaptchaId,
                                                     Locale locale,
                                                     @RequestHeader("jwtString") String jwtString) {
        try {
            if (jwtToken.verifyToken(jwtString , this.key)) {
                String[] captchaData = service.generateCaptchaImage(previousCaptchaId, locale);
                JsonObject response = new JsonObject();
                response.addProperty("captchaId", captchaData[1]);
                response.addProperty("captchaImg", captchaData[0]);
                response.addProperty("audioCaptcha", captchaData[2]);
                return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("You can't get acces , please give a valide token", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("sorry , you can't get access", HttpStatus.FORBIDDEN);
        }
    }

    /**
     * @param captchaId
     * @param captchaAnswer
     * @return fail or success as String response
     */

    @PostMapping(value = "/validateCaptcha/{captchaId}")
    public ResponseEntity<String> validateCaptcha(@PathVariable("captchaId") String captchaId,
                                                  @RequestParam(value = "captchaAnswer", required = false) String captchaAnswer,
                                                  @RequestParam(value = "useAudio", required = false) boolean useAudio,
                                                  @RequestHeader("jwtString") String jwtString) {
        if (captchaAnswer == null) {
            return new ResponseEntity<String>("Answer Captcha should not be empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (captchaId.trim().length() != 26) {
            return new ResponseEntity<String>("You should give a correcte CaptchaId", HttpStatus.BAD_REQUEST);
        }
        if (captchaAnswer != null && captchaAnswer.trim().length() != 8) {
            return new ResponseEntity<String>("You should give a valide Answer", HttpStatus.NOT_ACCEPTABLE);
        }
        if (jwtString == null) {
            return new ResponseEntity<String>("Sorry You can't get access", HttpStatus.FORBIDDEN);
        }

        try {
            if (jwtToken.verifyToken(jwtString , this.key)) {
                boolean responseCaptcha = false;
                responseCaptcha = service.validateCaptcha(captchaId, captchaAnswer, useAudio);
                JsonObject response = new JsonObject();
                response.addProperty("responseCaptcha", responseCaptcha ? "success" : "fail");
                return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("You can't get acces , please give a valide token", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("sorry , you can't get access", HttpStatus.FORBIDDEN);
        }
    }
}
