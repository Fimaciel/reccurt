package com.reccurt.reccurt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Corte e Religação - OpenMDX")
                        .description("Microsserviço para validação de comandos de corte e religação de energia")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe OpenMDX")
                                .email("suporte@openmdx.com")));
    }
}