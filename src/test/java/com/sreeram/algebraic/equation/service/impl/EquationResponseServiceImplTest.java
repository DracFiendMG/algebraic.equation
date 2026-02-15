package com.sreeram.algebraic.equation.service.impl;

import com.sreeram.algebraic.equation.exception.DivisionByZeroException;
import com.sreeram.algebraic.equation.exception.EquationNotFoundException;
import com.sreeram.algebraic.equation.exception.InvalidEquationException;
import com.sreeram.algebraic.equation.exception.VariableNotFoundException;
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
        equationService.clearAll();
    }

    @Test
    void testStoreEquation_Success() {
        String equation = "2*x + 3*y + z";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertNotNull(result.getEquationId());
        assertTrue(result.getEquationId() >= 1L);
        assertEquals("2x+3y+z", result.getEquation());
        assertNotNull(result.getExpressionTree());
    }

    @Test
    void testStoreEquation_ComplexExpression() {
        String equation = "(2*x + 3)*(y - z)";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertNotNull(result.getEquationId());
        assertEquals("(2x+3)(y-z)", result.getEquation());
        assertNotNull(result.getExpressionTree());
    }

    @Test
    void testStoreEquation_SimpleAddition() {
        String equation = "x + y";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertEquals("x+y", result.getEquation());
        assertNotNull(result.getExpressionTree());
    }

    @Test
    void testStoreEquation_WithPowerOperation() {
        String equation = "x^2 + y";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertEquals("x^2+y", result.getEquation());
        assertNotNull(result.getExpressionTree());
    }

    @Test
    void testStoreEquation_IncrementalIds() {
        String equation1 = "x + y";
        String equation2 = "2*x + 3*y";

        EquationResponse result1 = equationService.storeEquation(equation1);
        EquationResponse result2 = equationService.storeEquation(equation2);

        assertNotNull(result1.getEquationId());
        assertNotNull(result2.getEquationId());
        assertEquals(result1.getEquationId() + 1, result2.getEquationId());
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
        assertEquals("2x+3y+z", result.get(0).getEquation());
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

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

        assertNotNull(result);
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

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

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

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

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

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

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
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

        assertNotNull(result);
        assertEquals(5.0, result.getResult());
    }

    @Test
    void testEvaluateEquation_WithPower() {
        String equation = "x^y";
        EquationResponse stored = equationService.storeEquation(equation);

        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 3.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

        assertNotNull(result);
        assertEquals(8.0, result.getResult());
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

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

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

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

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

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

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

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

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

        EquationNotFoundException exception = assertThrows(EquationNotFoundException.class, () -> {
            equationService.evaluateEquation(999L, request);
        });

        assertTrue(exception.getMessage().contains("999"));
    }

    @Test
    void testEvaluateEquation_DivisionByZero() {
        String equation = "x / y";
        EquationResponse stored = equationService.storeEquation(equation);
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 10.0);
        variables.put("y", 0.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        DivisionByZeroException exception = assertThrows(DivisionByZeroException.class, () -> {
            equationService.evaluateEquation(stored.getEquationId(), request);
        });

        assertNotNull(exception.getMessage());
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

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

        assertNotNull(result);
        assertEquals(6000000.0, result.getResult());
    }

    @Test
    void testBuildExpressionTree_SimpleExpression() {
        String equation = "x + y";
        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result.getExpressionTree());
        assertEquals("+", result.getExpressionTree().getValue());
        assertNotNull(result.getExpressionTree().getLeft());
        assertNotNull(result.getExpressionTree().getRight());
    }

    @Test
    void testBuildExpressionTree_ComplexExpression() {
        String equation = "x * y + z";
        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result.getExpressionTree());
        assertEquals("+", result.getExpressionTree().getValue());
        assertTrue(result.getExpressionTree().isOperator());
        assertFalse(result.getExpressionTree().isLeaf());
    }

    @Test
    void testImplicitMultiplication_WithCoefficients() {
        String equation = "2x + 3y";
        EquationResponse stored = equationService.storeEquation(equation);

        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 5.0);
        variables.put("y", 4.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

        assertNotNull(result);
        assertEquals(22.0, result.getResult());
    }

    @Test
    void testImplicitMultiplication_NumberBeforeParenthesis() {
        String equation = "3(x + y)";
        EquationResponse stored = equationService.storeEquation(equation);

        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 4.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

        assertEquals(18.0, result.getResult());
    }

    @Test
    void testImplicitMultiplication_VariableBeforeVariable() {
        String equation = "xy + z";
        EquationResponse stored = equationService.storeEquation(equation);

        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 3.0);
        variables.put("y", 4.0);
        variables.put("z", 2.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

        assertEquals(14.0, result.getResult());
    }

    @Test
    void testImplicitMultiplication_ConsecutiveParentheses() {
        String equation = "(x + 1)(y + 2)";
        EquationResponse stored = equationService.storeEquation(equation);

        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 3.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

        assertEquals(15.0, result.getResult());
    }

    @Test
    void testImplicitMultiplication_VariableBeforeParenthesis() {
        String equation = "x(y + z)";
        EquationResponse stored = equationService.storeEquation(equation);

        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 3.0);
        variables.put("z", 4.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

        assertEquals(14.0, result.getResult());
    }

    @Test
    void testImplicitMultiplication_ParenthesisBeforeVariable() {
        String equation = "(x + y)z";
        EquationResponse stored = equationService.storeEquation(equation);

        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 3.0);
        variables.put("y", 2.0);
        variables.put("z", 4.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

        assertEquals(20.0, result.getResult());
    }

    @Test
    void testImplicitMultiplication_WithPower() {
        String equation = "2x^2 + 3y";
        EquationResponse stored = equationService.storeEquation(equation);

        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 3.0);
        variables.put("y", 2.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

        assertEquals(24.0, result.getResult());
    }

    @Test
    void testImplicitMultiplication_ComplexNested() {
        String equation = "2x(3y + 4z) + 5w";
        EquationResponse stored = equationService.storeEquation(equation);

        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 3.0);
        variables.put("z", 1.0);
        variables.put("w", 2.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

        assertEquals(62.0, result.getResult());
    }

    @Test
    void testImplicitMultiplication_DecimalCoefficients() {
        String equation = "2.5x + 1.5y";
        EquationResponse stored = equationService.storeEquation(equation);

        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 4.0);
        variables.put("y", 2.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), request);

        assertEquals(13.0, result.getResult());
    }

    @Test
    void testStoreEquation_NullEquation() {
        InvalidEquationException exception = assertThrows(InvalidEquationException.class, () -> {
            equationService.storeEquation(null);
        });

        assertTrue(exception.getMessage().contains("null or empty"));
    }

    @Test
    void testStoreEquation_EmptyEquation() {
        InvalidEquationException exception = assertThrows(InvalidEquationException.class, () -> {
            equationService.storeEquation("");
        });

        assertTrue(exception.getMessage().contains("null or empty"));
    }

    @Test
    void testStoreEquation_WhitespaceOnlyEquation() {
        InvalidEquationException exception = assertThrows(InvalidEquationException.class, () -> {
            equationService.storeEquation("   ");
        });

        assertTrue(exception.getMessage().contains("null or empty"));
    }

    @Test
    void testEvaluateEquation_MissingVariable() {
        String equation = "x + y + z";
        EquationResponse stored = equationService.storeEquation(equation);

        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 1.0);
        variables.put("y", 2.0);
        EvaluationRequest request = new EvaluationRequest(variables);

        VariableNotFoundException exception = assertThrows(VariableNotFoundException.class, () -> {
            equationService.evaluateEquation(stored.getEquationId(), request);
        });

        assertTrue(exception.getMessage().contains("z"));
    }

    @Test
    void testEvaluateEquation_AllVariablesMissing() {
        String equation = "x + y";
        EquationResponse stored = equationService.storeEquation(equation);

        Map<String, Double> variables = new HashMap<>();
        EvaluationRequest request = new EvaluationRequest(variables);

        assertThrows(VariableNotFoundException.class, () -> {
            equationService.evaluateEquation(stored.getEquationId(), request);
        });
    }
}

