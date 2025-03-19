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

    @GetMapping("/club/{idClub}/{fecha}/{hora}")
    public ResponseEntity<List<Pista>> getPistaByIdClub(@PathVariable Integer idClub, @PathVariable String fecha, @PathVariable String hora) {
        List<Pista> pistas = pistaService.getPistaByIdClub(idClub, fecha,hora+":00");
        if (pistas.isEmpty()) {
            //return ResponseEntity.notFound().build();  // No reservas encontradas
            return ResponseEntity.ok(pistas);  // Devolver las reservas encontradas, puede ser una lista vacia.
        }
        return ResponseEntity.ok(pistas);  // Devolver las reservas encontradas

    }

    @PostMapping
    public Pista createPista(@RequestBody Pista pista) {
        return pistaService.createPista(pista);
    }

    @PutMapping("")
    public ResponseEntity<Pista> updatePista(@RequestBody Pista pista) {
        pistaService.updatePista(pista);
        return ResponseEntity.ok(pista);
    }

    @DeleteMapping("/{nombre}/{email}")
    public ResponseEntity<Void> deletePista(@PathVariable String nombre, @PathVariable String email) {
        if (pistaService.deletePista(nombre, email)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}