package com.springboot.apirest.controller;


import com.springboot.apirest.dao.Reserva;
import com.springboot.apirest.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservas")
public class ReservasController {
    @Autowired
    private ReservaService reservasService;

    @GetMapping
    public List<Reserva> getReservas() {
        return reservasService.listarReservas();
    }





    /**
     * Obtener la lista de reservas por usuario.
     */

    @GetMapping("/{id}")
    public ResponseEntity<List<Reserva>> getReservaById(@PathVariable Integer id) {
        List<Reserva> reservas = reservasService.obtenerReservasDisponibles(id);
        if (reservas.isEmpty()) {
            return ResponseEntity.notFound().build();  // No reservas encontradas
        }
        return ResponseEntity.ok(reservas);  // Devolver las reservas encontradas
    }


    @GetMapping("/{id}/historico")
    public ResponseEntity<List<Reserva>> getReservaByIdHistorico(@PathVariable Integer id) {
        List<Reserva> reservas = reservasService.obtenerReservasDisponiblesHistorico(id);
        if (reservas.isEmpty()) {
            return ResponseEntity.notFound().build();  // No reservas encontradas
        }
        return ResponseEntity.ok(reservas);  // Devolver las reservas encontradas
    }




    @PostMapping
    public Reserva createReserva(@RequestBody Reserva reserva) {
        return reservasService.guardarReserva(reserva);
    }




    @PutMapping("/{id}")
    public ResponseEntity<Reserva> updateReserva(@PathVariable Integer id, @RequestBody Reserva reserva) {
        return reservasService.actualizarReserva(id, reserva)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }




    @PutMapping("/{id}/resultado")
    public ResponseEntity<Reserva> updateReservaResultado(@PathVariable Integer id, @RequestBody Map<String, String> resultado) {
        return reservasService.actualizarReservaResultado(id, resultado)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }




    @DeleteMapping("/{id}/{email}")
    public ResponseEntity<Void> deleteHistoricoReserva(@PathVariable Integer id, @PathVariable String email) {
        if (reservasService.eliminarReserva(id,email)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }




    @DeleteMapping()
    public ResponseEntity<Void> borrarReservasAntiguas() throws ParseException {
        reservasService.borrarReservasAnteriores();
        return ResponseEntity.ok().build();
    }




}
