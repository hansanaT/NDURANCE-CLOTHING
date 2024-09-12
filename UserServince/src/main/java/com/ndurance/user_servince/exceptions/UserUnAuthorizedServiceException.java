package com.ndurance.user_servince.exceptions;

public class UserUnAuthorizedServiceException extends RuntimeException{
    private static final long serialVersionUID = -8948771109171435607L;
    public UserUnAuthorizedServiceException(String message) {
        super(message);
    }
}
