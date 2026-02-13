package com.sreeram.algebraic.equation.service;

import com.sreeram.algebraic.equation.model.EquationResponse;
import com.sreeram.algebraic.equation.model.EvaluationRequest;
import com.sreeram.algebraic.equation.model.EvaluationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EquationService {
    EquationResponse storeEquation(String equation);
    List<EquationResponse> getAllEquations();
    EvaluationResponse evaluateEquation(EvaluationRequest request);
}

