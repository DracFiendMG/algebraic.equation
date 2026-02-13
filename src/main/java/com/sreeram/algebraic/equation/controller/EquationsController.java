package com.sreeram.algebraic.equation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/equations")
public class EquationsController {
    public ResponseEntity<String> storeEquation() {
        return ResponseEntity.ok("Equation stored successfully");
    }
}
