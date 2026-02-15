package com.sreeram.algebraic.equation.exception;

public class VariableNotFoundException extends RuntimeException {

    public VariableNotFoundException(String variableName) {
        super("Variable not found: " + variableName);
    }

    public VariableNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
