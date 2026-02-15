package com.sreeram.algebraic.equation.exception;

public class InvalidEquationException extends RuntimeException {

    public InvalidEquationException(String message) {
        super(message);
    }

    public InvalidEquationException(String message, Throwable cause) {
        super(message, cause);
    }
}
