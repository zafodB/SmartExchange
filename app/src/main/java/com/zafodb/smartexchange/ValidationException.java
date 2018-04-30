package com.zafodb.smartexchange;

/**
 * Validation exception class is used to indicate an error when validating user's input.
 *
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
