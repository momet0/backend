package com.tienda.services.impl;

import com.tienda.dtos.AuthResponse;
import com.tienda.dtos.LoginRequest;
import com.tienda.exceptions.TokenInvalidoException;
import com.tienda.models.RefreshToken;
import com.tienda.models.User;
import com.tienda.repositories.RefreshTokenRepository;
import com.tienda.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword())
        );

        String token = jwtService.generateToken(request.getUsername());
        String refreshToken = generarRefreshToken(userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario invalido")));

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .username(request.getUsername())
                .build();
    }

    public AuthResponse refreshToken(String refreshToken){

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new TokenInvalidoException("token invalido"));

        if(token.getFechaExpiracion().isBefore(LocalDateTime.now())){
            refreshTokenRepository.delete(token);
            throw new TokenInvalidoException("Token refresh expirado");
        }

        User user = token.getUser();

        String nuevoTokenAcceso = jwtService.generateToken(user.getUsername());
        String refreshTokenNuevo = UUID.randomUUID().toString();

        RefreshToken TokenNuevo = new RefreshToken();
        TokenNuevo.setToken(refreshTokenNuevo);
        TokenNuevo.setUser(user);
        TokenNuevo.setFechaExpiracion(LocalDateTime.now().plusDays(7));

        refreshTokenRepository.delete(token);

        refreshTokenRepository.save(TokenNuevo);

        return AuthResponse.builder()
                .token(nuevoTokenAcceso)
                .refreshToken(TokenNuevo.getToken())
                .username(user.getUsername())
                .build();
    }

    public void logOut(String refreshToken){
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }

    private String generarRefreshToken(User user){
        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setFechaExpiracion(LocalDateTime.now().plusDays(7));

        refreshTokenRepository.save(refreshToken);
        return token;
    }
}