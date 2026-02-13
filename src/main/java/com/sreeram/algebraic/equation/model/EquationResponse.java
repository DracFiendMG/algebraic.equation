package com.sreeram.algebraic.equation.model;

import java.util.Objects;

public class EquationResponse {

    private Long equationId;
    private String message;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquationResponse equationResponse = (EquationResponse) o;
        return Objects.equals(equationId, equationResponse.equationId) &&
               Objects.equals(message, equationResponse.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equationId, message);
    }

    @Override
    public String toString() {
        return "Equation{" +
                "id=" + equationId +
                ", expression='" + message + '\'' +
                '}';
    }
}

