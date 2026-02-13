package com.sreeram.algebraic.equation.service.impl;

import com.sreeram.algebraic.equation.model.EquationResponse;
import com.sreeram.algebraic.equation.model.EvaluationRequest;
import com.sreeram.algebraic.equation.model.EvaluationResponse;
import com.sreeram.algebraic.equation.service.EquationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquationServiceImpl implements EquationService {

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
