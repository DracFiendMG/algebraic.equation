package com.sreeram.algebraic.equation.exception;

public class EquationNotFoundException extends RuntimeException {

    public EquationNotFoundException(Long equationId) {
        super("Equation not found with id: " + equationId);
    }

    public EquationNotFoundException(String message) {
        super(message);
    }
}
