package com.financas.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Finanças API")
                        .version("1.0.0")
                        .description("API REST para gerenciamento de finanças pessoais com suporte a parcelas, despesas recorrentes e controle de dívidas.")
                        .contact(new Contact()
                                .name("José Segura")
                                .email("josehsegura2004@gmail.com")));
    }
}