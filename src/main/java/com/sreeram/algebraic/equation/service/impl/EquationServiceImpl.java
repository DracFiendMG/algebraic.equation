package com.sreeram.algebraic.equation.service.impl;

import com.sreeram.algebraic.equation.model.EquationResponse;
import com.sreeram.algebraic.equation.model.EvaluationRequest;
import com.sreeram.algebraic.equation.model.EvaluationResponse;
import com.sreeram.algebraic.equation.service.EquationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquationServiceImpl implements EquationService {
    char[] stack = {};
    int index = 0;

    public static int precedence(char c) {
        if (c == '(' || c == ')') {
            return 4;
        } else if (c == '^') {
            return 3;
        } else if (c == '*' || c == '/') {
            return 2;
        } else if (c == '+' || c == '-') {
            return 1;
        } else {
            return -1;
        }
    }

    public String convertToPostFix(String equation) {
        for (int i = 0; i < equation.length(); i++) {
            char currentChar = equation.charAt(i);
            int precedenceValue = precedence(currentChar);
            if (precedenceValue > 0) {
                stack[index++] = currentChar;
            }
        }

        return null;
    }

    @Override
    public EquationResponse storeEquation(String equation) {

        return null;
    }

    @Override
    public List<EquationResponse> getAllEquations() {
        return List.of();
    }

    @Override
    public EvaluationResponse evaluateEquation(EvaluationRequest request) {
        return null;
    }
}
