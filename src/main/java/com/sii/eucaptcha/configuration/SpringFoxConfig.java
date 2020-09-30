package com.sii.eucaptcha.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.Collections;
/**
 * @author mousab.aidoud
 * @version 1.0
 * Swagger class configuration
 */
@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    /**
     *
     * @return docket of swagger.
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sii.EuCaptcha.controller"))
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }
    /**
     * Customise API information's
     * @return API info's.
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "EU CAPTCHA REST API",
                "Some custom description of API.",
                "EU CAPTCHA 1.0 API",
                "Terms of service",
                new Contact("SII BERUXELLES", "http://www.groupe-sii.com/fr/filiale_belgique_bruxelles", "myeaddress@sii.be"),
                "License of API", "EUPL (European Union Public License)", Collections.emptyList());
    }
}