package com.sreeram.algebraic.equation.model;

import java.util.Map;

public class EvaluationRequest {

    private Map<String, Double> variables;

    public EvaluationRequest() {
    }

    public EvaluationRequest(Map<String, Double> variables) {
        this.variables = variables;
    }

    public Map<String, Double> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Double> variables) {
        this.variables = variables;
    }
}

