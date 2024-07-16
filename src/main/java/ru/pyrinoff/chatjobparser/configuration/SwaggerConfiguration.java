package ru.pyrinoff.chatjobparser.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SALARIES API Docs")
                        .description("Sort of documentation")
                        .version("v1.0.0")
                        .contact(
                                new Contact()
                                        .email("bigdick@production.hawaii")
                        )
                );
    }
}
