package com.sreeram.algebraic.equation.model;

import java.util.Map;

public class EvaluationResponse {

    private Long equationId;
    private String equation;
    private Map<String, Double> variable;
    private Double result;

    public EvaluationResponse() {
    }

    public Long getEquationId() {
        return equationId;
    }

    public void setEquationId(Long equationId) {
        this.equationId = equationId;
    }

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }

    public Map<String, Double> getVariable() {
        return variable;
    }

    public void setVariable(Map<String, Double> variable) {
        this.variable = variable;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }
}

