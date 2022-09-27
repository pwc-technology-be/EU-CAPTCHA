package com.sii.eucaptcha.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("EU Captcha Rest API",
                "API for use of EU Captcha",
                "1.0",
                "",
                new Contact("Digit info",
                        "",
                        "DIGIT-EU-CAPTCHA@ec.europa.eu"),
                "European Union Public License 1.2",
                "https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12",
                Collections.emptyList()
                );
    }
}
