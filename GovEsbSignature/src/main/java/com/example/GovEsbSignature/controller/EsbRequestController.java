package com.example.GovEsbSignature.controller;

import com.example.GovEsbSignature.dto.EsbRequestDTO;
import com.example.GovEsbSignature.service.EsbRequestService;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    public ResponseEntity<String> createEsbRequest(@RequestBody ObjectNode payloadDto) {
        String apiCode = payloadDto.get("apiCode").asText();
        String codeType = payloadDto.get("codeType").asText();
        try {
            String requestBody = esbRequestService.createEsbRequest(payloadDto, apiCode, codeType);
            return ResponseEntity.ok(requestBody);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace(); // Log the exception as needed
            return ResponseEntity.status(500).body("Error processing request: " + e.getMessage());
        }
    }
}

