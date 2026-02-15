package com.sreeram.algebraic.equation.controller;

import com.sreeram.algebraic.equation.model.EquationRequest;
import com.sreeram.algebraic.equation.model.EquationResponse;
import com.sreeram.algebraic.equation.model.EvaluationRequest;
import com.sreeram.algebraic.equation.model.EvaluationResponse;
import com.sreeram.algebraic.equation.service.EquationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquationsControllerTest {

    @Mock
    private EquationService equationService;

    @InjectMocks
    private EquationsController equationsController;

    private EquationRequest equationRequest;
    private EquationResponse equationResponse;
    private EvaluationRequest evaluationRequest;
    private EvaluationResponse evaluationResponse;

    @BeforeEach
    void setUp() {
        equationRequest = new EquationRequest();
        equationRequest.setEquation("x + y * 2");

        equationResponse = new EquationResponse();
        equationResponse.setEquationId(1L);
        equationResponse.setEquation("x + y * 2");
        equationResponse.setMessage("Equation stored successfully!");

        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 10.0);
        variables.put("y", 5.0);

        evaluationRequest = new EvaluationRequest();
        evaluationRequest.setVariables(variables);

        evaluationResponse = new EvaluationResponse();
        evaluationResponse.setResult(20.0);
        evaluationResponse.setMessage("Equation evaluated successfully!");
    }

    @Test
    void testStoreEquation_Success() {
        when(equationService.storeEquation(any(String.class))).thenReturn(equationResponse);

        ResponseEntity<EquationResponse> response = equationsController.storeEquation(equationRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getEquationId());
        assertEquals("x + y * 2", response.getBody().getEquation());
        assertEquals("Equation stored successfully!", response.getBody().getMessage());
    }

    @Test
    void testStoreEquation_NullRequest() {
        EquationRequest nullRequest = new EquationRequest();
        nullRequest.setEquation(null);

        EquationResponse errorResponse = new EquationResponse("Invalid equation");
        when(equationService.storeEquation(null)).thenReturn(errorResponse);

        ResponseEntity<EquationResponse> response = equationsController.storeEquation(nullRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Invalid equation", response.getBody().getMessage());
    }

    @Test
    void testGetAllEquations_Success() {
        EquationResponse equation1 = new EquationResponse(1L, "First equation");
        equation1.setEquation("x + y");

        EquationResponse equation2 = new EquationResponse(2L, "Second equation");
        equation2.setEquation("x * y");

        List<EquationResponse> equations = Arrays.asList(equation1, equation2);
        when(equationService.getAllEquations()).thenReturn(equations);

        ResponseEntity<List<EquationResponse>> response = equationsController.getAllEquations();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("x + y", response.getBody().get(0).getEquation());
        assertEquals("x * y", response.getBody().get(1).getEquation());
    }

    @Test
    void testGetAllEquations_EmptyList() {
        when(equationService.getAllEquations()).thenReturn(Arrays.asList());

        ResponseEntity<List<EquationResponse>> response = equationsController.getAllEquations();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testEvaluateEquation_Success() {
        when(equationService.evaluateEquation(eq(1L), any(EvaluationRequest.class))).thenReturn(evaluationResponse);

        ResponseEntity<EvaluationResponse> response = equationsController.evaluateEquation(1L, evaluationRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(20.0, response.getBody().getResult());
        assertEquals("Equation evaluated successfully!", response.getBody().getMessage());
    }

    @Test
    void testEvaluateEquation_EquationNotFound() {
        EvaluationResponse errorResponse = new EvaluationResponse("Equation not found");
        when(equationService.evaluateEquation(eq(999L), any(EvaluationRequest.class))).thenReturn(errorResponse);

        ResponseEntity<EvaluationResponse> response = equationsController.evaluateEquation(999L, evaluationRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Equation not found", response.getBody().getMessage());
    }

    @Test
    void testEvaluateEquation_EmptyVariables() {
        EvaluationRequest emptyRequest = new EvaluationRequest();
        emptyRequest.setVariables(new HashMap<>());

        EvaluationResponse emptyResponse = new EvaluationResponse();
        emptyResponse.setResult(0.0);
        emptyResponse.setMessage("Evaluated with empty variables");

        when(equationService.evaluateEquation(eq(1L), any(EvaluationRequest.class))).thenReturn(emptyResponse);

        ResponseEntity<EvaluationResponse> response = equationsController.evaluateEquation(1L, emptyRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(0.0, response.getBody().getResult());
    }

    @Test
    void testEvaluateEquation_ComplexExpression() {
        Map<String, Double> complexVariables = new HashMap<>();
        complexVariables.put("x", 3.0);
        complexVariables.put("y", 4.0);
        complexVariables.put("z", 2.0);

        EvaluationRequest complexRequest = new EvaluationRequest();
        complexRequest.setVariables(complexVariables);

        EvaluationResponse complexResponse = new EvaluationResponse();
        complexResponse.setResult(14.0);
        complexResponse.setMessage("Complex expression evaluated");

        when(equationService.evaluateEquation(eq(1L), any(EvaluationRequest.class))).thenReturn(complexResponse);

        ResponseEntity<EvaluationResponse> response = equationsController.evaluateEquation(1L, complexRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(14.0, response.getBody().getResult());
        assertEquals("Complex expression evaluated", response.getBody().getMessage());
    }
}


