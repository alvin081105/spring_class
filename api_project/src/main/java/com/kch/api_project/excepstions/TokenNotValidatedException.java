package com.kch.api_project.excepstions;

public class TokenNotValidatedException extends RuntimeException {
    public TokenNotValidatedException(String message) {
        super(message);
    }
}
