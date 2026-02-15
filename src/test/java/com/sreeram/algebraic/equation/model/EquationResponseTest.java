package com.sreeram.algebraic.equation.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquationResponseTest {

    @Test
    void testEquationCreation_WithIdAndMessage() {
        EquationResponse equationResponse = new EquationResponse(1L, "Equation stored successfully");

        assertEquals(1L, equationResponse.getEquationId());
        assertEquals("Equation stored successfully", equationResponse.getMessage());
        assertNull(equationResponse.getEquation());
        assertNull(equationResponse.getExpressionTree());
    }

    @Test
    void testEquationCreation_WithOnlyMessage() {
        EquationResponse equationResponse = new EquationResponse("Error occurred");

        assertNull(equationResponse.getEquationId());
        assertEquals("Error occurred", equationResponse.getMessage());
        assertNull(equationResponse.getEquation());
        assertNull(equationResponse.getExpressionTree());
    }

    @Test
    void testEquationCreation_DefaultConstructor() {
        EquationResponse equationResponse = new EquationResponse();

        assertNull(equationResponse.getEquationId());
        assertNull(equationResponse.getMessage());
        assertNull(equationResponse.getEquation());
        assertNull(equationResponse.getExpressionTree());
    }

    @Test
    void testEquationSetters() {
        EquationResponse equationResponse = new EquationResponse();
        ExpressionTreeNode tree = new ExpressionTreeNode("+");

        equationResponse.setEquationId(1L);
        equationResponse.setMessage("Equation stored successfully");
        equationResponse.setEquation("x + y");
        equationResponse.setExpressionTree(tree);

        assertEquals(1L, equationResponse.getEquationId());
        assertEquals("Equation stored successfully", equationResponse.getMessage());
        assertEquals("x + y", equationResponse.getEquation());
        assertNotNull(equationResponse.getExpressionTree());
        assertEquals("+", equationResponse.getExpressionTree().getValue());
    }

    @Test
    void testEquationGetters() {
        EquationResponse equationResponse = new EquationResponse();
        ExpressionTreeNode tree = new ExpressionTreeNode("*");

        equationResponse.setEquationId(2L);
        equationResponse.setMessage("Complex equation");
        equationResponse.setEquation("x * y");
        equationResponse.setExpressionTree(tree);

        assertEquals(2L, equationResponse.getEquationId());
        assertEquals("Complex equation", equationResponse.getMessage());
        assertEquals("x * y", equationResponse.getEquation());
        assertEquals("*", equationResponse.getExpressionTree().getValue());
    }

    @Test
    void testExpressionTreeSetting() {
        EquationResponse equationResponse = new EquationResponse();
        ExpressionTreeNode tree = new ExpressionTreeNode("+");
        tree.setLeft(new ExpressionTreeNode("x"));
        tree.setRight(new ExpressionTreeNode("y"));

        equationResponse.setExpressionTree(tree);

        assertNotNull(equationResponse.getExpressionTree());
        assertEquals("+", equationResponse.getExpressionTree().getValue());
        assertEquals("x", equationResponse.getExpressionTree().getLeft().getValue());
        assertEquals("y", equationResponse.getExpressionTree().getRight().getValue());
    }

    @Test
    void testComplexExpressionTree() {
        EquationResponse equationResponse = new EquationResponse();
        ExpressionTreeNode tree = new ExpressionTreeNode("*");
        ExpressionTreeNode leftSubtree = new ExpressionTreeNode("+");
        leftSubtree.setLeft(new ExpressionTreeNode("x"));
        leftSubtree.setRight(new ExpressionTreeNode("y"));
        tree.setLeft(leftSubtree);
        tree.setRight(new ExpressionTreeNode("z"));

        equationResponse.setExpressionTree(tree);
        equationResponse.setEquation("(x + y) * z");

        assertEquals("(x + y) * z", equationResponse.getEquation());
        assertEquals("*", equationResponse.getExpressionTree().getValue());
        assertEquals("+", equationResponse.getExpressionTree().getLeft().getValue());
        assertEquals("z", equationResponse.getExpressionTree().getRight().getValue());
    }

    @Test
    void testEquationResponseWithLongId() {
        EquationResponse equationResponse = new EquationResponse();
        equationResponse.setEquationId(999999L);
        equationResponse.setMessage("Large ID test");

        assertEquals(999999L, equationResponse.getEquationId());
        assertEquals("Large ID test", equationResponse.getMessage());
    }

    @Test
    void testNullExpressionTree() {
        EquationResponse equationResponse = new EquationResponse();
        equationResponse.setExpressionTree(null);

        assertNull(equationResponse.getExpressionTree());
    }
}

