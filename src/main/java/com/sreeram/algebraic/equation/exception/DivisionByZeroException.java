package com.sreeram.algebraic.equation.exception;

public class DivisionByZeroException extends ArithmeticException {

    public DivisionByZeroException() {
        super("Division by zero is not allowed");
    }

    public DivisionByZeroException(String message) {
        super(message);
    }
}
