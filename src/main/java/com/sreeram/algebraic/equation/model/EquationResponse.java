package com.sreeram.algebraic.equation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EquationResponse {

    private Long equationId;
    private String message;
    private String equation;
    @JsonIgnore
    private ExpressionTreeNode expressionTree;

    public EquationResponse() {
    }

    public EquationResponse(String message) {
        this.message = message;
    }

    public EquationResponse(Long equationId, String message) {
        this.equationId = equationId;
        this.message = message;
    }

    public Long getEquationId() {
        return equationId;
    }

    public void setEquationId(Long equationId) {
        this.equationId = equationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }

    public ExpressionTreeNode getExpressionTree() {
        return expressionTree;
    }

    public void setExpressionTree(ExpressionTreeNode expressionTree) {
        this.expressionTree = expressionTree;
    }
}

