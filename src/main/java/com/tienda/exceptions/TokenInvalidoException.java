package com.tienda.exceptions;

public class TokenInvalidoException extends RuntimeException{
    public TokenInvalidoException(String mensaje){
        super(mensaje);
    }
}
