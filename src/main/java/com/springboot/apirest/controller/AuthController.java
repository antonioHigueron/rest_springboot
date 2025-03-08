package com.springboot.apirest.controller;


import com.springboot.apirest.dto.Token;
import com.springboot.apirest.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        try {
            Token token = authService.login(loginData.get("email"), loginData.get("password"));
            //return ResponseEntity.ok(Map.of("token", token));
            Map<String, Object> mapa= Map.of("token", token.getToken(),"usuario", token.getUsuario());
            return ResponseEntity.ok(mapa);
            //return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }
}




