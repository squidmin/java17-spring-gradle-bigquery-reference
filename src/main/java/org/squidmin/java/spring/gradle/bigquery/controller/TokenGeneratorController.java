package org.squidmin.java.spring.gradle.bigquery.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.squidmin.java.spring.gradle.bigquery.service.GcpTokenService;

@RestController
@RequestMapping("/token-generator")
@Slf4j
public class TokenGeneratorController {

    private final GcpTokenService gcpTokenService;

    public TokenGeneratorController(GcpTokenService gcpTokenService) {
        this.gcpTokenService = gcpTokenService;
    }

    @PostMapping(value = "/generate-access-token")
    public ResponseEntity<String> generateToken() {
        return new ResponseEntity<>(gcpTokenService.generateAccessToken(), HttpStatus.OK);
    }

}
