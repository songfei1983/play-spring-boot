package com.example.workflow.controller;

import org.springframework.web.bind.annotation.*;
import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequestMapping("/api-key")
public class ApiKeyController {
    
    @GetMapping("/generate")
    public String generateApiKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}