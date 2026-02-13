package com.sreeram.algebraic.equation.service;

import com.sreeram.algebraic.equation.model.EquationResponse;
import com.sreeram.algebraic.equation.model.EvaluationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EquationResponseServiceTest {

    private EquationService equationService;

    @BeforeEach
    void setUp() {
        equationService = new EquationService();
    }

    @Test
    void testStoreEquation_ValidLinearEquation() {
        String equation = "2*x + 3 = 11";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertNotNull(result.getEquationId());
        assertEquals(equation, result.getMessage());
    }

    @Test
    void testStoreEquation_ValidQuadraticEquation() {
        String equation = "x^2 + 5*x + 6 = 0";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertEquals(equation, result.getMessage());
    }

    @Test
    void testStoreEquation_WithSpaces() {
        String equation = "  3 * x - 5 = 10  ";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertEquals(equation.trim(), result.getMessage());
    }

    @Test
    void testStoreEquation_InvalidFormat_NoEquals() {
        String equation = "2*x + 3";

        assertThrows(IllegalArgumentException.class, () -> {
            equationService.storeEquation(equation);
        });
    }

    @Test
    void testStoreEquation_InvalidFormat_MultipleEquals() {
        String equation = "2*x = 3 = 5";

        assertThrows(IllegalArgumentException.class, () -> {
            equationService.storeEquation(equation);
        });
    }

    @Test
    void testStoreEquation_Emptyequation() {
        assertThrows(IllegalArgumentException.class, () -> {
            equationService.storeEquation("");
        });
    }

    @Test
    void testStoreEquation_Nullequation() {
        assertThrows(IllegalArgumentException.class, () -> {
            equationService.storeEquation(null);
        });
    }

    @Test
    void testGetEquationById_Exists() {
        EquationResponse stored = equationService.storeEquation("2*x + 3 = 11");

        Optional<EquationResponse> result = equationService.getEquationById(stored.getEquationId());

        assertTrue(result.isPresent());
        assertEquals(stored.getEquationId(), result.get().getEquationId());
        assertEquals(stored.getMessage(), result.get().getMessage());
    }

    @Test
    void testGetEquationById_NotExists() {
        Optional<EquationResponse> result = equationService.getEquationById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllEquations_Empty() {
        List<EquationResponse> result = equationService.getAllEquations();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllEquations_MultipleEquations() {
        equationService.storeEquation("2*x + 3 = 11");
        equationService.storeEquation("3*x - 5 = 10");
        equationService.storeEquation("x^2 = 16");

        List<EquationResponse> result = equationService.getAllEquations();

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void testEvaluateEquation_SimpleLinearEquation() {
        EquationResponse stored = equationService.storeEquation("2*x + 3 = 11");
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 4.0);

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), variables);

        assertNotNull(result);
        assertTrue(result.isValid());
        assertNotNull(result.getResult());
        assertEquals(4.0, result.getResult(), 0.001);
    }

    @Test
    void testEvaluateEquation_InvalidValue() {
        EquationResponse stored = equationService.storeEquation("2*x + 3 = 11");
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 5.0);

        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), variables);

        assertNotNull(result);
        assertFalse(result.isValid());
    }

    @Test
    void testEvaluateEquation_MissingVariable() {
        EquationResponse stored = equationService.storeEquation("2*x + 3 = 11");
        Map<String, Double> variables = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> {
            equationService.evaluateEquation(stored.getEquationId(), variables);
        });
    }

    @Test
    void testEvaluateEquation_EquationNotFound() {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 4.0);

        assertThrows(IllegalArgumentException.class, () -> {
            equationService.evaluateEquation(999L, variables);
        });
    }

    @Test
    void testSolveEquation_LinearEquation() {
        EquationResponse stored = equationService.storeEquation("2*x + 3 = 11");

        EvaluationResponse result = equationService.solveEquation(stored.getEquationId());

        assertNotNull(result);
        assertTrue(result.isValid());
        assertEquals(4.0, result.getResult(), 0.001);
        assertNotNull(result.getSolution());
        assertTrue(result.getSolution().contains("x"));
    }

    @Test
    void testSolveEquation_AnotherLinearEquation() {
        EquationResponse stored = equationService.storeEquation("3*x - 5 = 10");

        EvaluationResponse result = equationService.solveEquation(stored.getEquationId());

        assertNotNull(result);
        assertTrue(result.isValid());
        assertEquals(5.0, result.getResult(), 0.001);
    }

    @Test
    void testSolveEquation_WithDivision() {
        EquationResponse stored = equationService.storeEquation("x/2 = 5");

        EvaluationResponse result = equationService.solveEquation(stored.getEquationId());

        assertNotNull(result);
        assertTrue(result.isValid());
        assertEquals(10.0, result.getResult(), 0.001);
    }

    @Test
    void testSolveEquation_EquationNotFound() {
        assertThrows(IllegalArgumentException.class, () -> {
            equationService.solveEquation(999L);
        });
    }

    @Test
    void testDeleteEquation_Exists() {
        EquationResponse stored = equationService.storeEquation("2*x + 3 = 11");

        boolean result = equationService.deleteEquation(stored.getEquationId());

        assertTrue(result);
        assertFalse(equationService.getEquationById(stored.getEquationId()).isPresent());
    }

    @Test
    void testDeleteEquation_NotExists() {
        boolean result = equationService.deleteEquation(999L);

        assertFalse(result);
    }

    @Test
    void testMultipleOperations() {
        EquationResponse eq1 = equationService.storeEquation("2*x + 3 = 11");
        EquationResponse eq2 = equationService.storeEquation("3*x - 5 = 10");

        EvaluationResponse solve1 = equationService.solveEquation(eq1.getEquationId());
        EvaluationResponse solve2 = equationService.solveEquation(eq2.getEquationId());

        List<EquationResponse> all = equationService.getAllEquations();

        assertEquals(2, all.size());
        assertEquals(4.0, solve1.getResult(), 0.001);
        assertEquals(5.0, solve2.getResult(), 0.001);
    }

    @Test
    void testComplexequation_WithParentheses() {
        String equation = "(2*x + 3) * 2 = 22";

        EquationResponse result = equationService.storeEquation(equation);

        assertNotNull(result);
        assertEquals(equation, result.getMessage());
    }

    @Test
    void testSolveEquation_WithParentheses() {
        EquationResponse stored = equationService.storeEquation("(2*x + 3) * 2 = 22");

        EvaluationResponse result = equationService.solveEquation(stored.getEquationId());

        assertNotNull(result);
        assertTrue(result.isValid());
        assertEquals(4.0, result.getResult(), 0.001);
    }
}

