package com.example.GovEsbSignature.controller;

import com.example.GovEsbSignature.dto.EsbRequestDTO;
import com.example.GovEsbSignature.service.EsbRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/esb")
public class EsbRequestController {

    private final EsbRequestService esbRequestService;

    public EsbRequestController(EsbRequestService esbRequestService) {
        this.esbRequestService = esbRequestService;
    }

    @PostMapping("/submit")
    public ResponseEntity<String> createEsbRequest(@RequestBody EsbRequestDTO esbRequestDTO, @RequestParam String apiCode) {
        try {
            String requestBody = esbRequestService.createEsbRequest(esbRequestDTO, apiCode);
            return ResponseEntity.ok("Request processed successfully: " + requestBody);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace(); // Log the exception as needed
            return ResponseEntity.status(500).body("Error processing request: " + e.getMessage());
        }
    }
}

