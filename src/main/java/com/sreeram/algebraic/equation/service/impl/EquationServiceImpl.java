package com.sreeram.algebraic.equation.service.impl;

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
    private static Long id = 0L;

    @Override
    public EquationResponse storeEquation(String equation) {
        ExpressionTreeNode tree = buildExpressionTree(equation);

        EquationResponse response = new EquationResponse();
        response.setEquationId(id);
        response.setEquation(equation);
        response.setExpressionTree(tree);
        response.setMessage("Equation stored successfully!");

        equationTrees.put(id++, response);

        return response;
    }

    @Override
    public List<EquationResponse> getAllEquations() {
        return equationTrees.keySet().stream().map(equationTrees::get).toList();
    }

    @Override
    public EvaluationResponse evaluateEquation(Long equationId, EvaluationRequest request) {
        EquationResponse storedEquation = equationTrees.get(equationId);
        if (storedEquation == null) {
            return new EvaluationResponse("Equation not found");
        }

        ExpressionTreeNode tree = storedEquation.getExpressionTree();
        ExpressionTreeNode evaluatedTree = substituteVariables(tree, request.getVariables());
        double result = evaluateTree(evaluatedTree);

        EvaluationResponse response = new EvaluationResponse();
        response.setResult(result);
        response.setMessage("Equation evaluated successfully!");

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
            throw new IllegalArgumentException("Invalid expression");
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
            throw new IllegalArgumentException("Null node in expression tree");
        }

        if (node.isLeaf()) {
            try {
                return Double.parseDouble(node.getValue());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid operand: " + node.getValue());
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
                    throw new ArithmeticException("Division by zero");
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
}
