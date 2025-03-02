package com.springboot.apirest.controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    // Endpoint para login (ya gestionado por Spring Security)
    @PostMapping("/login")
    public String login() {
        // Este endpoint será gestionado por Spring Security.
        return "Login Successful";
    }

    // Verificar si la sesión está activa
    @GetMapping("/verify_session")
    public String verifySession() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            User user = (User) principal;
            return "{\"status\": \"success\", \"message\": \"Sesión activa\", \"role\": \"" + user.getAuthorities() + "\"}";
        }
        return "{\"status\": \"error\", \"message\": \"Sesión no válida\"}";
    }

    // Endpoint para logout
    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext(); // Limpiar el contexto de seguridad
        return "{\"status\": \"success\", \"message\": \"Sesión cerrada\"}";
    }
}

