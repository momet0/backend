package com.tienda.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta de autenticación con token JWT")
public class AuthResponse {

    @Schema(description = "Token JWT que debe enviarse en el header Authorization",
            example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "token de refresco para poder obtener un nuevo token de acceso")
    private String refreshToken;

    @Schema(description = "username del usuario que está logueado")
    private String username;
}
