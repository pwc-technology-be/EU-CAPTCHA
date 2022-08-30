package com.sii.eucaptcha.controller.dto.captcharesult;

import com.sii.eucaptcha.controller.constants.CaptchaConstants;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class CaptchaResultDto implements Serializable {
    @ApiModelProperty(
            value = "Generated ID of the Captcha",
            name = "captchaId",
            dataType = "String",
            example = "mj0kvg8s39sufq9uj8cs5ckorj"
    )
    private String captchaId ;
    @ApiModelProperty(
            value = "The CaptchaImage",
            name = "captchaImg",
            dataType = "String",
            example = "iVBORw0KGgoAAAANSUhEUgAAAZAAAADICAIAAABJdyC//..."
    )
    private String captchaImg ;
    @ApiModelProperty(
            value = "Type of the Captcha",
            name = "captchaType",
            dataType = "String",
            example = "STANDARD for textual captcha or WHATS_UP for rotated image captcha"
    )
    private String captchaType = CaptchaConstants.TEXTUAL;

    public CaptchaResultDto() {
        super();
    }

    public String getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }

    public String getCaptchaImg() {
        return captchaImg;
    }

    public void setCaptchaImg(String captchaImg) {
        this.captchaImg = captchaImg;
    }


    public String getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(String captchaType) {
        this.captchaType = captchaType;
    }
}
