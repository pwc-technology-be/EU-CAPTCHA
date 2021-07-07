package com.sii.eucaptcha.controller;

import com.google.gson.JsonObject;
import com.sii.eucaptcha.controller.constants.CaptchaConstants;
import com.sii.eucaptcha.controller.dto.captcharesult.CaptchaResultDto;
import com.sii.eucaptcha.controller.dto.captchaquery.CaptchaQueryDto;
import com.sii.eucaptcha.service.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author mousab.aidoud
 * Captcha Rest Controller class with those methodes : getCaptchaImage , reloadCaptchaImage , validateCaptcha.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class CaptchaController {

    private final CaptchaService captchaService;

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

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }


    /**
     * Get the Captcha (Id + Captcha Image + Captcha Audio)
     *
     * @param locale the chosen locale
     * @return response as String contains CaptchaID and Captcha Image
     */
    @CrossOrigin
    @GetMapping(value = "/captchaImg")
    public CaptchaResultDto getCaptchaImage(@RequestParam(required = false) String locale,
                                            @RequestParam(required = false) Integer captchaLength,
                                            @RequestParam(defaultValue = CaptchaConstants.STANDARD, required = false) String captchaType,
                                            @RequestParam(defaultValue = "true", required = false) boolean capitalized,
                                            @RequestParam(required = false) Integer degree) {

        CaptchaQueryDto captchaQueryDto = new CaptchaQueryDto.CaptchaQueryDtoBuilder(captchaType)
                .captchaLength(captchaLength)
                .locale(locale)
                .degree(degree)
                .capitalized(capitalized)
                .build();

        return captchaService.generateCaptchaWrapper(captchaQueryDto);
    }

    /**
     * Reloading the captcha Image
     *
     * @param previousCaptchaId the ID of the previous Captcha
     * @param locale            the chosen Locale
     * @return response as String contains CaptchaID and Captcha Image
     */
    @CrossOrigin
    @GetMapping(value = "/reloadCaptchaImg/{previousCaptchaId}")
    public CaptchaResultDto reloadCaptchaImage(@PathVariable("previousCaptchaId") String previousCaptchaId,
                                               @RequestParam(required = false) String locale,
                                               @RequestParam(required = false) Integer captchaLength,
                                               @RequestParam(defaultValue = CaptchaConstants.STANDARD, required = false) String captchaType,
                                               @RequestParam(required = false) boolean capitalized,
                                               @RequestParam(required = false) Integer degree) {

        CaptchaQueryDto captchaQueryDto = new CaptchaQueryDto.CaptchaQueryDtoBuilder(captchaType)
                .captchaLength(captchaLength)
                .previousCaptchaId(previousCaptchaId)
                .locale(locale)
                .degree(degree)
                .capitalized(capitalized)
                .build();
        return captchaService.generateCaptchaWrapper(captchaQueryDto);
    }

    /**
     * Validating the captcha answer :
     *
     * @param captchaId     the ID of the Captcha
     * @param captchaAnswer the answer of the Captcha -> success or fail
     * @return fail or success as String response
     */
    @CrossOrigin
    @PostMapping(value = "/validateCaptcha/{captchaId}")
    public ResponseEntity<String> validateCaptcha(@PathVariable(value = "captchaId", required = false) String captchaId,
                                                  @RequestParam(value = "captchaAnswer", required = false) String captchaAnswer,
                                                  @RequestParam(value = "useAudio", required = false) boolean useAudio,
                                                  @RequestParam(value = "captchaType", defaultValue = CaptchaConstants.STANDARD, required = false) String captchaType) {


        //Verify the validity of the captcha answer.
        if (captchaId.trim().length() != captchaIdLength) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            try {
                boolean responseCaptcha;
                responseCaptcha = captchaService.validateCaptcha(captchaId, captchaAnswer, captchaType, useAudio);
                JsonObject response = new JsonObject();
                //response captcha ( valid -> success || invalid -> fail  )
                response.addProperty("responseCaptcha", responseCaptcha ? "success" : "fail");
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
    }

}