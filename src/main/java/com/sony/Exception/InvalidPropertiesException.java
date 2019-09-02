package com.sony.Exception;

public class InvalidPropertiesException extends RuntimeException {
    public InvalidPropertiesException(String errorMessage) {
        super(errorMessage);
    }
}
