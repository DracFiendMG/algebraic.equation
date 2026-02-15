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
        response.setMessage("Evaluation successful");

        assertEquals(1L, response.getEquationId());
        assertEquals("2*x + 3*y + z", response.getEquation());
        assertNotNull(response.getVariable());
        assertEquals(3, response.getVariable().size());
        assertEquals(2.0, response.getVariable().get("x"));
        assertEquals(3.0, response.getVariable().get("y"));
        assertEquals(4.0, response.getVariable().get("z"));
        assertEquals(17.0, response.getResult());
        assertEquals("Evaluation successful", response.getMessage());
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
        response.setMessage("Addition complete");

        assertEquals(2L, response.getEquationId());
        assertEquals("x + y + z", response.getEquation());
        assertEquals(variables, response.getVariable());
        assertEquals(30.0, response.getResult());
        assertEquals("Addition complete", response.getMessage());
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
        response.setMessage("Multiplication done");

        assertNotNull(response.getEquationId());
        assertNotNull(response.getEquation());
        assertNotNull(response.getVariable());
        assertNotNull(response.getResult());
        assertNotNull(response.getMessage());
    }

    @Test
    void testEvaluationResponse_WithMessageConstructor() {
        EvaluationResponse response = new EvaluationResponse("Error occurred");

        assertEquals("Error occurred", response.getMessage());
        assertNull(response.getEquationId());
        assertNull(response.getEquation());
        assertNull(response.getVariable());
        assertNull(response.getResult());
    }

    @Test
    void testEvaluationResponse_DefaultConstructor() {
        EvaluationResponse response = new EvaluationResponse();

        assertNull(response.getMessage());
        assertNull(response.getEquationId());
        assertNull(response.getEquation());
        assertNull(response.getVariable());
        assertNull(response.getResult());
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
        response.setMessage("Zero result");

        assertEquals(0.0, response.getResult());
        assertEquals(0.0, response.getVariable().get("x"));
        assertEquals(0.0, response.getVariable().get("y"));
        assertEquals(0.0, response.getVariable().get("z"));
        assertEquals("Zero result", response.getMessage());
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
        response.setMessage("Negative result");

        assertEquals(-6.0, response.getResult());
        assertEquals(-1.0, response.getVariable().get("x"));
        assertEquals(-2.0, response.getVariable().get("y"));
        assertEquals(-3.0, response.getVariable().get("z"));
        assertEquals("Negative result", response.getMessage());
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
        response.setMessage("Decimal calculation");

        assertEquals(7.5, response.getResult());
        assertEquals(1.5, response.getVariable().get("x"));
        assertEquals("Decimal calculation", response.getMessage());
    }

    @Test
    void testEvaluationResponse_VariableMapNotNull() {
        EvaluationResponse response = new EvaluationResponse();
        Map<String, Double> variables = new HashMap<>();

        response.setVariable(variables);
        response.setMessage("Empty variables");

        assertNotNull(response.getVariable());
        assertTrue(response.getVariable().isEmpty());
        assertEquals("Empty variables", response.getMessage());
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
        response.setMessage("Complex expression evaluated");

        assertEquals("(x + y) * z", response.getEquation());
        assertEquals(20.0, response.getResult());
        assertEquals("Complex expression evaluated", response.getMessage());
    }

    @Test
    void testEvaluationResponse_LargeEquationId() {
        EvaluationResponse response = new EvaluationResponse();

        response.setEquationId(999999L);
        response.setMessage("Large ID test");

        assertEquals(999999L, response.getEquationId());
        assertEquals("Large ID test", response.getMessage());
    }

    @Test
    void testEvaluationResponse_AllVariablesPresent() {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 10.0);
        variables.put("y", 20.0);
        variables.put("z", 30.0);

        EvaluationResponse response = new EvaluationResponse();
        response.setVariable(variables);
        response.setMessage("All variables set");

        assertTrue(response.getVariable().containsKey("x"));
        assertTrue(response.getVariable().containsKey("y"));
        assertTrue(response.getVariable().containsKey("z"));
        assertEquals(3, response.getVariable().size());
        assertEquals("All variables set", response.getMessage());
    }
}


