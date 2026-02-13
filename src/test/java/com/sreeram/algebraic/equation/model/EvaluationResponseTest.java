package com.sreeram.algebraic.equation.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EvaluationResponseTest {

    @Test
    void testEvaluationResponseCreation_WithAllFields() {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 3.0);
        variables.put("z", 4.0);

        EvaluationResponse response = new EvaluationResponse();
        response.setEquationId(1L);
        response.setEquation("2*x + 3*y + z");
        response.setVariable(variables);
        response.setResult(17.0);

        assertEquals(1L, response.getEquationId());
        assertEquals("2*x + 3*y + z", response.getEquation());
        assertNotNull(response.getVariable());
        assertEquals(3, response.getVariable().size());
        assertEquals(2.0, response.getVariable().get("x"));
        assertEquals(3.0, response.getVariable().get("y"));
        assertEquals(4.0, response.getVariable().get("z"));
        assertEquals(17.0, response.getResult());
    }

    @Test
    void testEvaluationResponseSetters() {
        EvaluationResponse response = new EvaluationResponse();
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 5.0);
        variables.put("y", 10.0);
        variables.put("z", 15.0);

        response.setEquationId(2L);
        response.setEquation("x + y + z");
        response.setVariable(variables);
        response.setResult(30.0);

        assertEquals(2L, response.getEquationId());
        assertEquals("x + y + z", response.getEquation());
        assertEquals(variables, response.getVariable());
        assertEquals(30.0, response.getResult());
    }

    @Test
    void testEvaluationResponseGetters() {
        EvaluationResponse response = new EvaluationResponse();
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 1.0);
        variables.put("y", 2.0);
        variables.put("z", 3.0);

        response.setEquationId(3L);
        response.setEquation("x * y * z");
        response.setVariable(variables);
        response.setResult(6.0);

        assertNotNull(response.getEquationId());
        assertNotNull(response.getEquation());
        assertNotNull(response.getVariable());
        assertNotNull(response.getResult());
    }

    @Test
    void testEvaluationResponse_WithZeroResult() {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 0.0);
        variables.put("y", 0.0);
        variables.put("z", 0.0);

        EvaluationResponse response = new EvaluationResponse();
        response.setEquationId(4L);
        response.setEquation("x + y + z");
        response.setVariable(variables);
        response.setResult(0.0);

        assertEquals(0.0, response.getResult());
        assertEquals(0.0, response.getVariable().get("x"));
        assertEquals(0.0, response.getVariable().get("y"));
        assertEquals(0.0, response.getVariable().get("z"));
    }

    @Test
    void testEvaluationResponse_WithNegativeValues() {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", -1.0);
        variables.put("y", -2.0);
        variables.put("z", -3.0);

        EvaluationResponse response = new EvaluationResponse();
        response.setEquationId(5L);
        response.setEquation("x + y + z");
        response.setVariable(variables);
        response.setResult(-6.0);

        assertEquals(-6.0, response.getResult());
        assertEquals(-1.0, response.getVariable().get("x"));
        assertEquals(-2.0, response.getVariable().get("y"));
        assertEquals(-3.0, response.getVariable().get("z"));
    }

    @Test
    void testEvaluationResponse_WithDecimalValues() {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 1.5);
        variables.put("y", 2.5);
        variables.put("z", 3.5);

        EvaluationResponse response = new EvaluationResponse();
        response.setEquationId(6L);
        response.setEquation("x + y + z");
        response.setVariable(variables);
        response.setResult(7.5);

        assertEquals(7.5, response.getResult());
        assertEquals(1.5, response.getVariable().get("x"));
    }

    @Test
    void testEvaluationResponse_VariableMapNotNull() {
        EvaluationResponse response = new EvaluationResponse();
        Map<String, Double> variables = new HashMap<>();

        response.setVariable(variables);

        assertNotNull(response.getVariable());
        assertTrue(response.getVariable().isEmpty());
    }

    @Test
    void testEvaluationResponse_ComplexExpression() {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 3.0);
        variables.put("z", 4.0);

        EvaluationResponse response = new EvaluationResponse();
        response.setEquationId(7L);
        response.setEquation("(x + y) * z");
        response.setVariable(variables);
        response.setResult(20.0);

        assertEquals("(x + y) * z", response.getEquation());
        assertEquals(20.0, response.getResult());
    }

    @Test
    void testEvaluationResponse_LargeEquationId() {
        EvaluationResponse response = new EvaluationResponse();

        response.setEquationId(999999L);

        assertEquals(999999L, response.getEquationId());
    }

    @Test
    void testEvaluationResponse_AllVariablesPresent() {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 10.0);
        variables.put("y", 20.0);
        variables.put("z", 30.0);

        EvaluationResponse response = new EvaluationResponse();
        response.setVariable(variables);

        assertTrue(response.getVariable().containsKey("x"));
        assertTrue(response.getVariable().containsKey("y"));
        assertTrue(response.getVariable().containsKey("z"));
        assertEquals(3, response.getVariable().size());
    }
}


