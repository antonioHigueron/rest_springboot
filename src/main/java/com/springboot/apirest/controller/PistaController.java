package com.springboot.apirest.controller;

import com.springboot.apirest.dao.Pista;
import com.springboot.apirest.service.PistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pistas")
public class PistaController {
    @Autowired
    private PistaService pistaService;

    @GetMapping
    public List<Pista> getAllPistas() {
        return pistaService.getAllPistas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pista> getPistaById(@PathVariable Integer id) {
        return pistaService.getPistaById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Pista createPista(@RequestBody Pista pista) {
        return pistaService.createPista(pista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pista> updatePista(@PathVariable Integer id, @RequestBody Pista pista) {
        return pistaService.updatePista(id, pista)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePista(@PathVariable Integer id) {
        if (pistaService.deletePista(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}