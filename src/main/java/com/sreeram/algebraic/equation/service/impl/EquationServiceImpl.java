package com.sreeram.algebraic.equation.service.impl;

import com.sreeram.algebraic.equation.exception.DivisionByZeroException;
import com.sreeram.algebraic.equation.exception.EquationNotFoundException;
import com.sreeram.algebraic.equation.exception.InvalidEquationException;
import com.sreeram.algebraic.equation.exception.VariableNotFoundException;
import com.sreeram.algebraic.equation.model.EquationResponse;
import com.sreeram.algebraic.equation.model.EvaluationRequest;
import com.sreeram.algebraic.equation.model.EvaluationResponse;
import com.sreeram.algebraic.equation.model.ExpressionTreeNode;
import com.sreeram.algebraic.equation.model.Stack;
import com.sreeram.algebraic.equation.service.EquationService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EquationServiceImpl implements EquationService {
    private static final Map<Long, EquationResponse> equationTrees = new HashMap<>();
    private static Long id = 1L;

    public void clearAll() {
        equationTrees.clear();
        id = 1L;
    }

    @Override
    public EquationResponse storeEquation(String equation) {
        if (equation == null || equation.trim().isEmpty()) {
            throw new InvalidEquationException("Equation cannot be null or empty");
        }

        ExpressionTreeNode tree = buildExpressionTree(equation);

        EquationResponse response = new EquationResponse();
        response.setEquationId(id);
        response.setEquation(convertTreeToInfix(tree));
        response.setExpressionTree(tree);

        equationTrees.put(id++, response);

        return response;
    }

    @Override
    public List<EquationResponse> getAllEquations() {
        return equationTrees.keySet().stream().map(key -> {
            EquationResponse response = equationTrees.get(key);
            EquationResponse returnResponse = new EquationResponse();
            returnResponse.setEquationId(response.getEquationId());
            returnResponse.setEquation(convertTreeToInfix(response.getExpressionTree()));
            return returnResponse;
        }).toList();
    }

    @Override
    public EvaluationResponse evaluateEquation(Long equationId, EvaluationRequest request) {
        EquationResponse storedEquation = equationTrees.get(equationId);
        if (storedEquation == null) {
            throw new EquationNotFoundException(equationId);
        }

        ExpressionTreeNode tree = storedEquation.getExpressionTree();
        ExpressionTreeNode evaluatedTree = substituteVariables(tree, request.getVariables());
        double result = evaluateTree(evaluatedTree);

        EvaluationResponse response = new EvaluationResponse();
        response.setResult(result);
        response.setEquation(convertTreeToInfix(tree));
        response.setEquationId(storedEquation.getEquationId());
        response.setVariable(request.getVariables());

        return response;
    }

    private ExpressionTreeNode buildExpressionTree(String equation) {
        String preprocessedEquation = preprocessEquation(equation);
        String postfix = convertToPostfix(preprocessedEquation);
        return buildTreeFromPostfix(postfix);
    }

    private String preprocessEquation(String equation) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < equation.length(); i++) {
            char current = equation.charAt(i);
            result.append(current);

            if (i < equation.length() - 1) {
                char next = equation.charAt(i + 1);

                if (shouldInsertMultiplication(current, next)) {
                    result.append('*');
                }
            }
        }

        return result.toString();
    }

    private boolean shouldInsertMultiplication(char current, char next) {
        if (Character.isWhitespace(current) || Character.isWhitespace(next)) {
            return false;
        }

        boolean currentIsDigit = Character.isDigit(current);
        boolean currentIsLetter = Character.isAlphabetic(current);
        boolean nextIsLetter = Character.isAlphabetic(next);
        boolean nextIsDigit = Character.isDigit(next);
        boolean nextIsOpenParen = next == '(';
        boolean currentIsCloseParen = current == ')';

        if (currentIsDigit && nextIsLetter) {
            return true;
        }

        if (currentIsLetter && nextIsLetter) {
            return true;
        }

        if (currentIsDigit && nextIsOpenParen) {
            return true;
        }

        if (currentIsLetter && nextIsDigit) {
            return true;
        }

        if (currentIsLetter && nextIsOpenParen) {
            return true;
        }

        if (currentIsCloseParen && nextIsDigit) {
            return true;
        }

        if (currentIsCloseParen && nextIsLetter) {
            return true;
        }

        if (currentIsCloseParen && nextIsOpenParen) {
            return true;
        }

        return false;
    }

    private String convertToPostfix(String equation) {
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();

        for (int i = 0; i < equation.length(); i++) {
            char currentChar = equation.charAt(i);

            if (Character.isWhitespace(currentChar)) {
                continue;
            }

            if (Character.isAlphabetic(currentChar) || Character.isDigit(currentChar)) {
                StringBuilder operand = new StringBuilder();
                while (i < equation.length() &&
                       (Character.isAlphabetic(equation.charAt(i)) ||
                        Character.isDigit(equation.charAt(i)) ||
                        equation.charAt(i) == '.')) {
                    operand.append(equation.charAt(i));
                    i++;
                }
                i--;
                postfix.append(operand).append(" ");
            } else if (currentChar == '(') {
                stack.push(currentChar);
            } else if (currentChar == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(" ");
                }
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else if (isOperator(currentChar)) {
                while (!stack.isEmpty() &&
                       stack.peek() != '(' &&
                       (getPrecedence(stack.peek()) > getPrecedence(currentChar) ||
                        (getPrecedence(stack.peek()) == getPrecedence(currentChar) &&
                         !isRightAssociative(currentChar)))) {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.push(currentChar);
            }
        }

        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(" ");
        }

        return postfix.toString().trim();
    }

    private ExpressionTreeNode buildTreeFromPostfix(String postfix) {
        Stack<ExpressionTreeNode> stack = new Stack<>();
        String[] tokens = postfix.split("\\s+");

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            ExpressionTreeNode node = new ExpressionTreeNode(token);

            if (isOperator(token.charAt(0)) && token.length() == 1) {
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("Invalid expression");
                }
                node.setRight(stack.pop());
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("Invalid expression");
                }
                node.setLeft(stack.pop());
            }

            stack.push(node);
        }

        if (stack.isEmpty()) {
            throw new InvalidEquationException("Invalid expression");
        }

        return stack.pop();
    }

    private ExpressionTreeNode substituteVariables(ExpressionTreeNode node, Map<String, Double> variables) {
        if (node == null) {
            return null;
        }

        ExpressionTreeNode newNode = new ExpressionTreeNode(node.getValue());

        if (node.isLeaf()) {
            if (variables.containsKey(node.getValue())) {
                newNode.setValue(variables.get(node.getValue()).toString());
            }
            return newNode;
        }

        newNode.setLeft(substituteVariables(node.getLeft(), variables));
        newNode.setRight(substituteVariables(node.getRight(), variables));

        return newNode;
    }

    private double evaluateTree(ExpressionTreeNode node) {
        if (node == null) {
            throw new InvalidEquationException("Null node in expression tree");
        }

        if (node.isLeaf()) {
            try {
                return Double.parseDouble(node.getValue());
            } catch (NumberFormatException e) {
                throw new VariableNotFoundException(node.getValue());
            }
        }

        double leftValue = evaluateTree(node.getLeft());
        double rightValue = evaluateTree(node.getRight());

        return switch (node.getValue()) {
            case "+" -> leftValue + rightValue;
            case "-" -> leftValue - rightValue;
            case "*" -> leftValue * rightValue;
            case "/" -> {
                if (rightValue == 0) {
                    throw new DivisionByZeroException();
                }
                yield leftValue / rightValue;
            }
            case "^" -> Math.pow(leftValue, rightValue);
            default -> throw new IllegalArgumentException("Unknown operator: " + node.getValue());
        };
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private int getPrecedence(char c) {
        return switch (c) {
            case '^' -> 3;
            case '*', '/' -> 2;
            case '+', '-' -> 1;
            default -> -1;
        };
    }

    private boolean isRightAssociative(char c) {
        return c == '^';
    }

    private String convertTreeToInfix(ExpressionTreeNode node) {
        if (node == null) {
            return "";
        }

        if (node.isLeaf()) {
            return node.getValue();
        }

        String left = convertTreeToInfix(node.getLeft());
        String right = convertTreeToInfix(node.getRight());
        String operator = node.getValue();

        boolean needsLeftParens = shouldAddParentheses(node.getLeft(), node, true);
        boolean needsRightParens = shouldAddParentheses(node.getRight(), node, false);

        if (needsLeftParens) {
            left = "(" + left + ")";
        }
        if (needsRightParens) {
            right = "(" + right + ")";
        }

        if (operator.equals("*") && canUseImplicitMultiplication(left, right)) {
            return left + right;
        }

        return left + operator + right;
    }

    private boolean canUseImplicitMultiplication(String left, String right) {
        if (left.isEmpty() || right.isEmpty()) {
            return false;
        }

        char lastCharOfLeft = left.charAt(left.length() - 1);
        char firstCharOfRight = right.charAt(0);

        boolean leftEndsWithDigit = Character.isDigit(lastCharOfLeft);
        boolean leftEndsWithLetter = Character.isAlphabetic(lastCharOfLeft);
        boolean leftEndsWithParen = lastCharOfLeft == ')';

        boolean rightStartsWithLetter = Character.isAlphabetic(firstCharOfRight);
        boolean rightStartsWithParen = firstCharOfRight == '(';

        if (leftEndsWithDigit && rightStartsWithLetter) {
            return true;
        }

        if (leftEndsWithLetter && rightStartsWithLetter) {
            return true;
        }

        if (leftEndsWithDigit && rightStartsWithParen) {
            return true;
        }

        if (leftEndsWithLetter && rightStartsWithParen) {
            return true;
        }

        if (leftEndsWithParen && rightStartsWithLetter) {
            return true;
        }

        if (leftEndsWithParen && rightStartsWithParen) {
            return true;
        }

        return false;
    }

    private boolean shouldAddParentheses(ExpressionTreeNode child, ExpressionTreeNode parent, boolean isLeft) {
        if (child == null || child.isLeaf()) {
            return false;
        }

        if (!child.isOperator() || !parent.isOperator()) {
            return false;
        }

        int childPrecedence = getPrecedence(child.getValue().charAt(0));
        int parentPrecedence = getPrecedence(parent.getValue().charAt(0));

        if (childPrecedence < parentPrecedence) {
            return true;
        }

        if (childPrecedence == parentPrecedence) {
            if (!isLeft && (parent.getValue().equals("-") || parent.getValue().equals("/"))) {
                return true;
            }
            if (!isLeft && parent.getValue().equals("^")) {
                return true;
            }
        }

        return false;
    }
}
