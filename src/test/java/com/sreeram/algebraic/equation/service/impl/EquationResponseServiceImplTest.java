package com.sreeram.algebraic.equation.service.impl;

import com.sreeram.algebraic.equation.model.EquationResponse;
import com.sreeram.algebraic.equation.model.EvaluationRequest;
import com.sreeram.algebraic.equation.model.EvaluationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EquationResponseServiceImplTest {

    private EquationServiceImpl equationService;

    @BeforeEach
    void setUp() {
        equationService = new EquationServiceImpl();
    }

    @Test
    void testStoreEquation_Success() {
        String equation = "2*x + 3*y + z";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertEquals("Equation stored successfully", result.getMessage());
        assertNotNull(result.getEquationId());
        assertTrue(result.getEquationId() > 0);
    }

    @Test
    void testStoreEquation_ComplexExpression() {
        String equation = "5*x + 2*y - 3*z + 10";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertEquals("Equation stored successfully", result.getMessage());
        assertNotNull(result.getEquationId());
    }

    @Test
    void testStoreEquation_WithParentheses() {
        String equation = "(2*x + 3)*(y - z)";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertEquals("Equation stored successfully", result.getMessage());
        assertNotNull(result.getEquationId());
    }

    @Test
    void testStoreEquation_WithDivision() {
        String equation = "x/2 + y/3 + z";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertEquals("Equation stored successfully", result.getMessage());
        assertNotNull(result.getEquationId());
    }

    @Test
    void testStoreEquation_EmptyEquation() {
        String equation = "";

        assertThrows(IllegalArgumentException.class, () -> {
            equationService.storeEquation(equation);
        });
    }

    @Test
    void testStoreEquation_NullEquation() {
        assertThrows(IllegalArgumentException.class, () -> {
            equationService.storeEquation(null);
        });
    }

    @Test
    void testStoreEquation_OnlyConstant() {
        String equation = "42";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertEquals("Equation stored successfully", result.getMessage());
        assertNotNull(result.getEquationId());
    }

    @Test
    void testStoreEquation_SingleVariable() {
        String equation = "5*x";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertEquals("Equation stored successfully", result.getMessage());
        assertNotNull(result.getEquationId());
    }

    @Test
    void testStoreEquation_TwoVariables() {
        String equation = "3*x + 4*y";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertEquals("Equation stored successfully", result.getMessage());
        assertNotNull(result.getEquationId());
    }

    @Test
    void testStoreEquation_AllThreeVariables() {
        String equation = "x + y + z";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertEquals("Equation stored successfully", result.getMessage());
        assertNotNull(result.getEquationId());
    }

    @Test
    void testStoreEquation_IncrementalIds() {
        String equation1 = "x + y + z";
        String equation2 = "2*x + 3*y";

        EquationResponse result1 = equationService.storeEquation(equation1);
        EquationResponse result2 = equationService.storeEquation(equation2);

        assertNotNull(result1.getEquationId());
        assertNotNull(result2.getEquationId());
        assertNotEquals(result1.getEquationId(), result2.getEquationId());
    }

    @Test
    void testGetAllEquations_EmptyList() {
        List<EquationResponse> result = equationService.getAllEquations();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllEquations_SingleEquation() {
        String equation = "2*x + 3*y + z";
        EquationResponse stored = equationService.storeEquation(equation);

        List<EquationResponse> result = equationService.getAllEquations();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(stored.getEquationId(), result.get(0).getEquationId());
        assertEquals(equation, result.get(0).getEquation());
    }

    @Test
    void testGetAllEquations_MultipleEquations() {
        String equation1 = "x + y + z";
        String equation2 = "2*x - y";
        String equation3 = "3*z";

        EquationResponse stored1 = equationService.storeEquation(equation1);
        EquationResponse stored2 = equationService.storeEquation(equation2);
        EquationResponse stored3 = equationService.storeEquation(equation3);

        List<EquationResponse> result = equationService.getAllEquations();

        assertNotNull(result);
        assertEquals(3, result.size());
        
        assertTrue(result.stream().anyMatch(eq -> eq.getEquationId().equals(stored1.getEquationId())));
        assertTrue(result.stream().anyMatch(eq -> eq.getEquationId().equals(stored2.getEquationId())));
        assertTrue(result.stream().anyMatch(eq -> eq.getEquationId().equals(stored3.getEquationId())));
    }

    @Test
    void testEvaluateEquation_Success() {
        String equation = "2*x + 3*y + z";
        EquationResponse stored = equationService.storeEquation(equation);
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 3.0);
        variables.put("z", 4.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(1L, request);

        assertNotNull(result);
        assertEquals(stored.getEquationId(), result.getEquationId());
        assertEquals(equation, result.getEquation());
        assertNotNull(result.getVariable());
        assertEquals(2.0, result.getVariable().get("x"));
        assertEquals(3.0, result.getVariable().get("y"));
        assertEquals(4.0, result.getVariable().get("z"));
        assertEquals(17.0, result.getResult());
    }

    @Test
    void testEvaluateEquation_SimpleAddition() {
        String equation = "x + y + z";
        EquationResponse stored = equationService.storeEquation(equation);
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 1.0);
        variables.put("y", 2.0);
        variables.put("z", 3.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(1L, request);

        assertNotNull(result);
        assertEquals(6.0, result.getResult());
    }

    @Test
    void testEvaluateEquation_WithSubtraction() {
        String equation = "x - y - z";
        EquationResponse stored = equationService.storeEquation(equation);
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 10.0);
        variables.put("y", 3.0);
        variables.put("z", 2.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(1L, request);

        assertNotNull(result);
        assertEquals(5.0, result.getResult());
    }

    @Test
    void testEvaluateEquation_WithMultiplication() {
        String equation = "x * y * z";
        EquationResponse stored = equationService.storeEquation(equation);
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 3.0);
        variables.put("z", 4.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(1L, request);

        assertNotNull(result);
        assertEquals(24.0, result.getResult());
    }

    @Test
    void testEvaluateEquation_WithDivision() {
        String equation = "x / y";
        EquationResponse stored = equationService.storeEquation(equation);
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 10.0);
        variables.put("y", 2.0);
        variables.put("z", 0.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(1L, request);

        assertNotNull(result);
        assertEquals(5.0, result.getResult());
    }

    @Test
    void testEvaluateEquation_ComplexExpression() {
        String equation = "(x + y) * z";
        EquationResponse stored = equationService.storeEquation(equation);
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 3.0);
        variables.put("z", 4.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(1L, request);

        assertNotNull(result);
        assertEquals(20.0, result.getResult());
    }

    @Test
    void testEvaluateEquation_WithZeroValues() {
        String equation = "x + y + z";
        EquationResponse stored = equationService.storeEquation(equation);
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 0.0);
        variables.put("y", 0.0);
        variables.put("z", 0.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(1L, request);

        assertNotNull(result);
        assertEquals(0.0, result.getResult());
    }

    @Test
    void testEvaluateEquation_WithNegativeValues() {
        String equation = "x + y + z";
        EquationResponse stored = equationService.storeEquation(equation);
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", -1.0);
        variables.put("y", -2.0);
        variables.put("z", -3.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(1L, request);

        assertNotNull(result);
        assertEquals(-6.0, result.getResult());
    }

    @Test
    void testEvaluateEquation_WithDecimalValues() {
        String equation = "x + y + z";
        EquationResponse stored = equationService.storeEquation(equation);
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 1.5);
        variables.put("y", 2.5);
        variables.put("z", 3.5);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(1L, request);

        assertNotNull(result);
        assertEquals(7.5, result.getResult());
    }

    @Test
    void testEvaluateEquation_EquationNotFound() {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 1.0);
        variables.put("y", 2.0);
        variables.put("z", 3.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        assertThrows(IllegalArgumentException.class, () -> {
            equationService.evaluateEquation(1L, request);
        });
    }

    @Test
    void testEvaluateEquation_MissingVariables() {
        String equation = "x + y + z";
        EquationResponse stored = equationService.storeEquation(equation);
        
        Map<String, Double> variables = new HashMap<>();
        EvaluationRequest request = new EvaluationRequest(variables);

        assertThrows(IllegalArgumentException.class, () -> {
            equationService.evaluateEquation(1L, request);
        });
    }

    @Test
    void testEvaluateEquation_PartialVariables() {
        String equation = "x + y + z";
        EquationResponse stored = equationService.storeEquation(equation);
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 1.0);
        variables.put("y", 2.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        assertThrows(IllegalArgumentException.class, () -> {
            equationService.evaluateEquation(1L, request);
        });
    }

    @Test
    void testEvaluateEquation_DivisionByZero() {
        String equation = "x / y";
        EquationResponse stored = equationService.storeEquation(equation);
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 10.0);
        variables.put("y", 0.0);
        variables.put("z", 1.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        assertThrows(ArithmeticException.class, () -> {
            equationService.evaluateEquation(1L, request);
        });
    }

    @Test
    void testEvaluateEquation_LargeNumbers() {
        String equation = "x + y + z";
        EquationResponse stored = equationService.storeEquation(equation);
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 1000000.0);
        variables.put("y", 2000000.0);
        variables.put("z", 3000000.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(1L, request);

        assertNotNull(result);
        assertEquals(6000000.0, result.getResult());
    }

    @Test
    void testEvaluateEquation_ResponseContainsAllFields() {
        String equation = "x + y + z";
        EquationResponse stored = equationService.storeEquation(equation);
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 1.0);
        variables.put("y", 2.0);
        variables.put("z", 3.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(1L, request);

        assertNotNull(result.getEquationId());
        assertNotNull(result.getEquation());
        assertNotNull(result.getVariable());
        assertNotNull(result.getResult());
    }
}

