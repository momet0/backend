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
@Schema(description = "Datos necesarios para la autenticación de usuario")
public class LoginRequest {

    @Schema(description = "Nombre de usuario o email",
            example = "admin@tienda.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "Contraseña del usuario",
            example = "admin123",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
