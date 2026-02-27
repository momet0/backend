package com.tienda.controllers;

import com.tienda.dtos.AuthResponse;
import com.tienda.dtos.LoginRequest;
import com.tienda.dtos.RefreshTokenRequest;
import com.tienda.services.impl.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para autenticación de usuarios")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Autenticar usuario",
            description = "Inicia sesión del usuario y retorna token JWT (temporalmente desactivado por desarrollo)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                      "username": "admin"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "Usuario o contraseña incorrectos"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de request inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "El usuario es requerido"
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refrescar el token de acceso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Nuevo token de acceso generado"),
            @ApiResponse(responseCode = "401", description = "Refresh token invalido o expirado")
    })
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request){
        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar la sesion")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequest request){
        authService.logOut(request.getRefreshToken());
        return ResponseEntity.ok().build();
    }
}