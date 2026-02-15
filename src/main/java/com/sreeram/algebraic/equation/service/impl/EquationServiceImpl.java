package com.sreeram.algebraic.equation.service.impl;

import com.sreeram.algebraic.equation.model.EquationResponse;
import com.sreeram.algebraic.equation.model.EvaluationRequest;
import com.sreeram.algebraic.equation.model.EvaluationResponse;
import com.sreeram.algebraic.equation.model.Stack;
import com.sreeram.algebraic.equation.service.EquationService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EquationServiceImpl implements EquationService {
    private static final Map<Long, EquationResponse> postfixEquations = new HashMap<>();
    private static Long id = 0L;

    @Override
    public EquationResponse storeEquation(String equation) {
        String postfix = convertToPostFix(equation);

        EquationResponse response = new EquationResponse();
        response.setEquation(equation);
        response.setPostfix(postfix);
        response.setMessage("Equation stored successfully!");

        postfixEquations.put(id++, response);

        return response;
    }

    @Override
    public List<EquationResponse> getAllEquations() {
        return postfixEquations.keySet().stream().map(postfixEquations::get).toList();
    }

    @Override
    public EvaluationResponse evaluateEquation(Long equationId, EvaluationRequest request) {
        return null;
    }

    private String convertToPostFix(String equation) {
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();

        for (int i = 0; i < equation.length(); i++) {
            char currentChar = equation.charAt(i);

            if ((currentChar >= 'a' && currentChar <= 'z')
                    || (currentChar >= 'A' && currentChar <= 'Z')
                    || (currentChar >= '0' && currentChar <= '9')) {
                postfix.append(currentChar);
            } else if (currentChar == '(') {
                stack.push(currentChar);
            } else if (currentChar == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                stack.pop();
            } else {
                while (!stack.isEmpty()
                        && stack.peek() != '('
                        && (stack.precedence(stack.peek()) > stack.precedence(currentChar)
                        || (stack.precedence(stack.peek()) == stack.precedence(currentChar)
                        && !stack.isRightAssociative(currentChar)))) {
                    postfix.append(stack.pop());
                }
            }

            while (!stack.isEmpty()) {
                postfix.append(stack.pop());
            }
        }

        return postfix.toString();
    }

    private String replaceVariables(Long equationId, EvaluationRequest request) {
        String postfixEquation = postfixEquations.get(equationId).getPostfix();
        StringBuilder postfixEquationWithValues = new StringBuilder(postfixEquation);

        request.getVariables().entrySet().forEach((variable) -> {
            String variableName = variable.getKey();
            Double value = variable.getValue();

            postfixEquationWithValues.replace(0, postfixEquation.length(), postfixEquation.replaceAll(variableName, value.toString()));
        });


    }

    private String evaluatePostfix(String[] equation) {
        Stack<Integer> stack = new Stack<>();

        for (String character: equation) {
            if (Character.isDigit(character.charAt(0))
                    || (character.length() > 1
                    && character.charAt(0) == '-')) {
                stack.push(Integer.parseInt(character));
            } else {
                int value1 = stack.pop();
                int value2 = stack.pop();
            }
        }
    }
}
