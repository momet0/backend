package com.tienda.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "api de tienda",
        version = "1.0.0",
        description = "sistema de gestion de tienda con productos pedidos y stock",
        contact = @Contact(
            name = "desarrollo tienda",
            email = "dev@tienda.com"
        ),
        license = @License(
            name = "mit license",
            url = "https://opensource.org/licenses/mit"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "servidor de desarrollo"
        )
    },
    security = {
        @SecurityRequirement(name = "bearerAuth")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    description = "jwt authorization token",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "jwt",
    in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {
}