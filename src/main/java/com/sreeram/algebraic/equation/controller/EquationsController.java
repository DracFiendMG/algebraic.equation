package com.sreeram.algebraic.equation.controller;

import com.sreeram.algebraic.equation.model.EquationRequest;
import com.sreeram.algebraic.equation.model.EquationResponse;
import com.sreeram.algebraic.equation.model.EvaluationRequest;
import com.sreeram.algebraic.equation.model.EvaluationResponse;
import com.sreeram.algebraic.equation.service.EquationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equations")
public class EquationsController {

    private final EquationService equationService;

    public EquationsController(EquationService equationService) {
        this.equationService = equationService;
    }

    @PostMapping("/store")
    public ResponseEntity<EquationResponse> storeEquation(
            @RequestBody EquationRequest request
    ) {
        EquationResponse response = equationService.storeEquation(request.getEquation());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EquationResponse>> getAllEquations() {
        List<EquationResponse> equations = equationService.getAllEquations();
        return ResponseEntity.ok(equations);
    }

    @PostMapping("/{equationId}/evaluate")
    public ResponseEntity<EvaluationResponse> evaluateEquation(
            @PathVariable Long equationId,
            @RequestBody EvaluationRequest request
    ) {
        EvaluationResponse response = equationService.evaluateEquation(equationId, request);
        return ResponseEntity.ok(response);
    }
}
