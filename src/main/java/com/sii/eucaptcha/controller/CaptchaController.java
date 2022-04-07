package com.sii.eucaptcha.controller;

import com.google.gson.JsonObject;
import com.sii.eucaptcha.controller.constants.CaptchaConstants;
import com.sii.eucaptcha.controller.dto.captcharesult.CaptchaResultDto;
import com.sii.eucaptcha.controller.dto.captchaquery.CaptchaQueryDto;
import com.sii.eucaptcha.service.CaptchaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


/**
 * @author mousab.aidoud
 * Captcha Rest Controller class with those methodes : getCaptchaImage , reloadCaptchaImage , validateCaptcha.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class CaptchaController {

    private final CaptchaService captchaService;

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
    @ApiOperation(value = "Get a Captcha image",
            notes = "Returns a captcha image as per locale, captchaLength, type and capitalization or not")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved captcha image"),
            @ApiResponse(code = 406, message = "Not Acceptable - Locale is missing or invalid")
    })
    @GetMapping(value = "/captchaImg")
    public CaptchaResultDto getCaptchaImage(@RequestParam(defaultValue = "en-GB", required = false) String locale,
                                            @RequestParam(defaultValue = "8", required = false) Integer captchaLength,
                                            @RequestParam(defaultValue = CaptchaConstants.STANDARD, required = false) String captchaType,
                                            @RequestParam(defaultValue = "true", required = false) boolean capitalized,
                                            @RequestParam(required = false) Integer degree) {

        if (StringUtils.isBlank(locale) || StringUtils.equalsIgnoreCase("Undefined", locale)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Locale is missing or invalid!");
        }

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
    @ApiOperation(value = "Refresh a previous Captcha image",
            notes = "Returns a new captcha image as per locale, captchaLength, type and capitalization or not")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved captcha image"),
            @ApiResponse(code = 400, message = "CaptchaId is missing"),
            @ApiResponse(code = 406, message = "Not Acceptable - Locale is missing or invalid")
    })
    @GetMapping(value = "/reloadCaptchaImg/{previousCaptchaId}")
    public CaptchaResultDto reloadCaptchaImage(@PathVariable("previousCaptchaId") String previousCaptchaId,
                                               @RequestParam(required = false) String locale,
                                               @RequestParam(required = false) Integer captchaLength,
                                               @RequestParam(defaultValue = CaptchaConstants.STANDARD, required = false) String captchaType,
                                               @RequestParam(required = false) boolean capitalized,
                                               @RequestParam(required = false) Integer degree) {

        if (StringUtils.isBlank(locale) || StringUtils.equalsIgnoreCase("Undefined", locale)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Locale is missing or invalid!");
        }

        if (StringUtils.isBlank(previousCaptchaId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CaptchaId is missing!");
        }


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
    @ApiOperation(value = "Validate a Captcha image",
            notes = "Returns success or failed as an answer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfull response can be success or fail"),
            @ApiResponse(code = 400, message = "CaptchaId is missing")
    })
    @PostMapping(value = "/validateCaptcha/{captchaId}")
    public ResponseEntity<String> validateCaptcha(@PathVariable(value = "captchaId", required = false) String captchaId,
                                                  @RequestParam(value = "captchaAnswer", required = false) String captchaAnswer,
                                                  @RequestParam(value = "useAudio", required = false) boolean useAudio,
                                                  @RequestParam(value = "captchaType", defaultValue = CaptchaConstants.STANDARD, required = false) String captchaType) {


        //check if captchaId is present
        if (StringUtils.isBlank(captchaId)) {
            return new ResponseEntity<>("CaptchaId is missing!", HttpStatus.BAD_REQUEST);
        } else {
            //Verify the validity of the captcha answer.
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