package com.sreeram.algebraic.equation.exception;

import com.sreeram.algebraic.equation.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EquationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEquationNotFoundException(EquationNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("EQUATION_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidEquationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEquationException(InvalidEquationException ex) {
        ErrorResponse error = new ErrorResponse("INVALID_EQUATION", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DivisionByZeroException.class)
    public ResponseEntity<ErrorResponse> handleDivisionByZeroException(DivisionByZeroException ex) {
        ErrorResponse error = new ErrorResponse("DIVISION_BY_ZERO", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(VariableNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVariableNotFoundException(VariableNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("VARIABLE_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse("INTERNAL_ERROR", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
