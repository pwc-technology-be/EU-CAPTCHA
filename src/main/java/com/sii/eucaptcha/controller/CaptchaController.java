package com.sii.eucaptcha.controller;

import com.google.gson.JsonObject;
import com.sii.eucaptcha.controller.constants.CaptchaConstants;
import com.sii.eucaptcha.controller.dto.captcharesult.CaptchaResultDto;
import com.sii.eucaptcha.controller.dto.captchaquery.CaptchaQueryDto;
import com.sii.eucaptcha.exceptions.ForbiddenException;
import com.sii.eucaptcha.security.JwtToken;
import com.sii.eucaptcha.service.CaptchaService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@CrossOrigin
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
    // TODO add more parameters to specify the typeof Captcha CAP-23
    @GetMapping(value = "/captchaImg")
    public CaptchaResultDto getCaptchaImage(@ApiParam(hidden = true) Locale locale ,
                                            @ApiParam(hidden = true)  Integer captchaLength ,
                                            @ApiParam(defaultValue = CaptchaConstants.STANDARD) String captchaType ,
                                            @ApiParam() Integer degree) {


        CaptchaQueryDto captchaQueryDto = new CaptchaQueryDto.CaptchaQueryDtoBuilder(captchaType)
                .captchaLength(captchaLength)
                .locale(locale)
                .degree(degree)
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
    @GetMapping(value = "/reloadCaptchaImg/{previousCaptchaId}")
    public CaptchaResultDto reloadCaptchaImage(@PathVariable("previousCaptchaId") String previousCaptchaId,
                                               Locale locale,
                                               @ApiParam(hidden = true)  Integer captchaLength ,
                                               @ApiParam(defaultValue = CaptchaConstants.STANDARD) String captchaType ,
                                               @ApiParam() Integer degree,
                                               @RequestHeader("x-jwtString") String jwtString) {

        CaptchaQueryDto captchaQueryDto = new CaptchaQueryDto.CaptchaQueryDtoBuilder(captchaType)
                .captchaLength(captchaLength)
                .previousCaptchaId(previousCaptchaId)
                .locale(locale)
                .degree(degree)
                .build();


        try {
            // Reload the captcha if the token is valid
            if (jwtToken.verifyToken(jwtString)) {
                return captchaService.generateCaptchaWrapper(captchaQueryDto);
               // return captchaService.generateCaptchaImage(previousCaptchaId, locale);
            } else {
               throw new ForbiddenException();
            }
        } catch (Exception e) {
            throw new ForbiddenException();
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
                                                  @RequestParam(value = "captchaType" , required = true , defaultValue = CaptchaConstants.STANDARD ) String captchaType,
                                                  @RequestHeader("x-jwtString") String jwtString ) {


        //Verify the validity of the captcha answer.
        if (/*captchaAnswer.trim().length() != captchaAnswerLength || */
                captchaId.trim().length() != captchaIdLength
                || jwtString == null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        } else {
            try {
                if (jwtToken.verifyToken(jwtString)) {
                    boolean responseCaptcha;
                    //the Token is valid , we proceed the validation of the captcha
                    responseCaptcha = captchaService.validateCaptcha(captchaId, captchaAnswer , captchaType ,  useAudio );
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

}