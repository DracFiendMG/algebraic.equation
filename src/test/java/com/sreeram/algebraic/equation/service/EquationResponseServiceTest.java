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
        String expression = "2*x + 3 = 11";

        EquationResponse result = equationService.storeEquation(expression);

        assertNotNull(result);
        assertNotNull(result.getEquationId());
        assertEquals(expression, result.getMessage());
    }

    @Test
    void testStoreEquation_ValidQuadraticEquation() {
        String expression = "x^2 + 5*x + 6 = 0";

        EquationResponse result = equationService.storeEquation(expression);

        assertNotNull(result);
        assertEquals(expression, result.getMessage());
    }

    @Test
    void testStoreEquation_WithSpaces() {
        String expression = "  3 * x - 5 = 10  ";

        EquationResponse result = equationService.storeEquation(expression);

        assertNotNull(result);
        assertEquals(expression.trim(), result.getMessage());
    }

    @Test
    void testStoreEquation_InvalidFormat_NoEquals() {
        String expression = "2*x + 3";

        assertThrows(IllegalArgumentException.class, () -> {
            equationService.storeEquation(expression);
        });
    }

    @Test
    void testStoreEquation_InvalidFormat_MultipleEquals() {
        String expression = "2*x = 3 = 5";

        assertThrows(IllegalArgumentException.class, () -> {
            equationService.storeEquation(expression);
        });
    }

    @Test
    void testStoreEquation_EmptyExpression() {
        assertThrows(IllegalArgumentException.class, () -> {
            equationService.storeEquation("");
        });
    }

    @Test
    void testStoreEquation_NullExpression() {
        assertThrows(IllegalArgumentException.class, () -> {
            equationService.storeEquation(null);
        });
    }

    @Test
    void testGetEquationById_Exists() {
        // Arrange
        EquationResponse stored = equationService.storeEquation("2*x + 3 = 11");

        // Act
        Optional<EquationResponse> result = equationService.getEquationById(stored.getEquationId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(stored.getEquationId(), result.get().getEquationId());
        assertEquals(stored.getMessage(), result.get().getMessage());
    }

    @Test
    void testGetEquationById_NotExists() {
        // Act
        Optional<EquationResponse> result = equationService.getEquationById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllEquations_Empty() {
        // Act
        List<EquationResponse> result = equationService.getAllEquations();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllEquations_MultipleEquations() {
        // Arrange
        equationService.storeEquation("2*x + 3 = 11");
        equationService.storeEquation("3*x - 5 = 10");
        equationService.storeEquation("x^2 = 16");

        // Act
        List<EquationResponse> result = equationService.getAllEquations();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void testEvaluateEquation_SimpleLinearEquation() {
        // Arrange
        EquationResponse stored = equationService.storeEquation("2*x + 3 = 11");
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 4.0);

        // Act
        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), variables);

        // Assert
        assertNotNull(result);
        assertTrue(result.isValid());
        assertNotNull(result.getResult());
        assertEquals(4.0, result.getResult(), 0.001);
    }

    @Test
    void testEvaluateEquation_InvalidValue() {
        // Arrange
        EquationResponse stored = equationService.storeEquation("2*x + 3 = 11");
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 5.0);

        // Act
        EvaluationResponse result = equationService.evaluateEquation(stored.getEquationId(), variables);

        // Assert
        assertNotNull(result);
        assertFalse(result.isValid());
    }

    @Test
    void testEvaluateEquation_MissingVariable() {
        // Arrange
        EquationResponse stored = equationService.storeEquation("2*x + 3 = 11");
        Map<String, Double> variables = new HashMap<>();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            equationService.evaluateEquation(stored.getEquationId(), variables);
        });
    }

    @Test
    void testEvaluateEquation_EquationNotFound() {
        // Arrange
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 4.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            equationService.evaluateEquation(999L, variables);
        });
    }

    @Test
    void testSolveEquation_LinearEquation() {
        // Arrange - equation: 2*x + 3 = 11, solution: x = 4
        EquationResponse stored = equationService.storeEquation("2*x + 3 = 11");

        // Act
        EvaluationResponse result = equationService.solveEquation(stored.getEquationId());

        // Assert
        assertNotNull(result);
        assertTrue(result.isValid());
        assertEquals(4.0, result.getResult(), 0.001);
        assertNotNull(result.getSolution());
        assertTrue(result.getSolution().contains("x"));
    }

    @Test
    void testSolveEquation_AnotherLinearEquation() {
        // Arrange - equation: 3*x - 5 = 10, solution: x = 5
        EquationResponse stored = equationService.storeEquation("3*x - 5 = 10");

        // Act
        EvaluationResponse result = equationService.solveEquation(stored.getEquationId());

        // Assert
        assertNotNull(result);
        assertTrue(result.isValid());
        assertEquals(5.0, result.getResult(), 0.001);
    }

    @Test
    void testSolveEquation_WithDivision() {
        // Arrange - equation: x/2 = 5, solution: x = 10
        EquationResponse stored = equationService.storeEquation("x/2 = 5");

        // Act
        EvaluationResponse result = equationService.solveEquation(stored.getEquationId());

        // Assert
        assertNotNull(result);
        assertTrue(result.isValid());
        assertEquals(10.0, result.getResult(), 0.001);
    }

    @Test
    void testSolveEquation_EquationNotFound() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            equationService.solveEquation(999L);
        });
    }

    @Test
    void testDeleteEquation_Exists() {
        // Arrange
        EquationResponse stored = equationService.storeEquation("2*x + 3 = 11");

        // Act
        boolean result = equationService.deleteEquation(stored.getEquationId());

        // Assert
        assertTrue(result);
        assertFalse(equationService.getEquationById(stored.getEquationId()).isPresent());
    }

    @Test
    void testDeleteEquation_NotExists() {
        // Act
        boolean result = equationService.deleteEquation(999L);

        // Assert
        assertFalse(result);
    }

    @Test
    void testMultipleOperations() {
        // Arrange & Act
        EquationResponse eq1 = equationService.storeEquation("2*x + 3 = 11");
        EquationResponse eq2 = equationService.storeEquation("3*x - 5 = 10");

        EvaluationResponse solve1 = equationService.solveEquation(eq1.getEquationId());
        EvaluationResponse solve2 = equationService.solveEquation(eq2.getEquationId());

        List<EquationResponse> all = equationService.getAllEquations();

        // Assert
        assertEquals(2, all.size());
        assertEquals(4.0, solve1.getResult(), 0.001);
        assertEquals(5.0, solve2.getResult(), 0.001);
    }

    @Test
    void testComplexExpression_WithParentheses() {
        // Arrange
        String expression = "(2*x + 3) * 2 = 22";

        // Act
        EquationResponse result = equationService.storeEquation(expression);

        // Assert
        assertNotNull(result);
        assertEquals(expression, result.getMessage());
    }

    @Test
    void testSolveEquation_WithParentheses() {
        // Arrange - equation: (2*x + 3) * 2 = 22, solution: x = 4
        EquationResponse stored = equationService.storeEquation("(2*x + 3) * 2 = 22");

        // Act
        EvaluationResponse result = equationService.solveEquation(stored.getEquationId());

        // Assert
        assertNotNull(result);
        assertTrue(result.isValid());
        assertEquals(4.0, result.getResult(), 0.001);
    }
}

