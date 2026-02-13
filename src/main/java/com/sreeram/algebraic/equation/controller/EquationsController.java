package com.sreeram.algebraic.equation.controller;

import com.sreeram.algebraic.equation.model.EquationRequest;
import com.sreeram.algebraic.equation.model.EquationResponse;
import com.sreeram.algebraic.equation.model.EvaluationResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equations")
public class EquationsController {

    @PostMapping("/store")
    public EquationResponse storeEquation(
            @RequestBody EquationRequest request
    ) {
        return new EquationResponse();
    }

    @GetMapping
    public List<EquationResponse> getAllEquations(
    ) {
        return List.of();
    }

    @PostMapping("/{equationId}/evaluate")
    public EvaluationResponse evaluateEquation(
            @PathVariable Long equationId,
            @RequestBody EquationRequest request
    ) {
        return new EvaluationResponse();
    }
}
