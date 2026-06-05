package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 1. Informações gerais da API (Info)
                .info(new Info()
                        .title("API Clínica Veterinária")
                        .version("1.0.0")
                        .description("API REST para gerenciamento de usuários, pets, veterinários e consultas. " +
                                "Desenvolvida seguindo as melhores práticas arquiteturais.")
                        .contact(new Contact()
                                .name("Suporte UNIPÊ")
                                .email("suporte@unipe.br")
                                .url("https://unipe.br"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                // 2. Definição de Servidores (Servers) - Útil para separar ambientes
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Servidor Local (Desenvolvimento)"),
                        new Server().url("https://api.exemplo.com").description("Servidor de Produção")
                ))
                // 3. Configuração de Segurança Global (Exemplo com Bearer Token/JWT)
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Insira o token JWT gerado no login.")
                        ))
                // Adiciona o requisito de segurança para todos os endpoints (opcional, pode ser feito por rota)
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
    }
}

