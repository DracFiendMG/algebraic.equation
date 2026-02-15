package com.sreeram.algebraic.equation.model;

public class ExpressionTreeNode {
    private String value;
    private ExpressionTreeNode left;
    private ExpressionTreeNode right;

    public ExpressionTreeNode(String value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ExpressionTreeNode getLeft() {
        return left;
    }

    public void setLeft(ExpressionTreeNode left) {
        this.left = left;
    }

    public ExpressionTreeNode getRight() {
        return right;
    }

    public void setRight(ExpressionTreeNode right) {
        this.right = right;
    }

    public boolean isOperator() {
        return "+".equals(value) || "-".equals(value) || "*".equals(value) ||
               "/".equals(value) || "^".equals(value);
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}
