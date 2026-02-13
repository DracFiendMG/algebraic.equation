package com.sreeram.algebraic.equation.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquationResponseTest {

    @Test
    void testEquationCreation_WithIdAndExpression() {
        EquationResponse equationResponse = new EquationResponse(1L, "2*x + 3 = 11");

        assertEquals(1L, equationResponse.getEquationId());
        assertEquals("2*x + 3 = 11", equationResponse.getMessage());
    }

    @Test
    void testEquationCreation_WithOnlyExpression() {
        EquationResponse equationResponse = new EquationResponse("2*x + 3 = 11");

        assertNull(equationResponse.getEquationId());
        assertEquals("2*x + 3 = 11", equationResponse.getMessage());
    }

    @Test
    void testEquationSetters() {
        EquationResponse equationResponse = new EquationResponse();

        equationResponse.setEquationId(1L);
        equationResponse.setMessage("2*x + 3 = 11");

        assertEquals(1L, equationResponse.getEquationId());
        assertEquals("2*x + 3 = 11", equationResponse.getMessage());
    }

    @Test
    void testEquationEquality() {
        EquationResponse eq1 = new EquationResponse(1L, "2*x + 3 = 11");
        EquationResponse eq2 = new EquationResponse(1L, "2*x + 3 = 11");
        EquationResponse eq3 = new EquationResponse(2L, "3*x - 5 = 10");

        assertEquals(eq1, eq2);
        assertNotEquals(eq1, eq3);
    }

    @Test
    void testEquationHashCode() {
        EquationResponse eq1 = new EquationResponse(1L, "2*x + 3 = 11");
        EquationResponse eq2 = new EquationResponse(1L, "2*x + 3 = 11");

        assertEquals(eq1.hashCode(), eq2.hashCode());
    }

    @Test
    void testEquationToString() {
        EquationResponse equationResponse = new EquationResponse(1L, "2*x + 3 = 11");

        String result = equationResponse.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("2*x + 3 = 11"));
    }
}

