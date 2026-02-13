package com.sreeram.algebraic.equation.model;

public class EquationRequest {

    private String equation;

    public EquationRequest() {
    }

    public EquationRequest(String equation) {
        this.equation = equation;
    }

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }
}
