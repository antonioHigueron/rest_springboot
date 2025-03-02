package com.springboot.apirest.controller;

import com.springboot.apirest.dao.HistoricoReserva;
import com.springboot.apirest.service.HistoricoReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historicoReservas")
public class HistoricoReservasController {
    @Autowired
    private HistoricoReservaService historicoReservasService;

    @GetMapping
    public List<HistoricoReserva> getAllHistoricoReservas() {
        return historicoReservasService.getAllHistoricoReservas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoricoReserva> getHistoricoReservaById(@PathVariable Integer id) {
        return historicoReservasService.getHistoricoReservaById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public HistoricoReserva createHistoricoReserva(@RequestBody HistoricoReserva historicoReserva) {
        return historicoReservasService.createHistoricoReserva(historicoReserva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoricoReserva> updateHistoricoReserva(@PathVariable Integer id, @RequestBody HistoricoReserva historicoReserva) {
        return historicoReservasService.updateHistoricoReserva(id, historicoReserva)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoricoReserva(@PathVariable Integer id) {
        if (historicoReservasService.deleteHistoricoReserva(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
