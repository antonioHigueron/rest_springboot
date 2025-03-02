package com.springboot.apirest.service;

import com.springboot.apirest.dao.Club;
import com.springboot.apirest.dao.Reserva;
import com.springboot.apirest.repository.ClubRepository;
import com.springboot.apirest.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> obtenerReservaPorId(Integer id) {
        return reservaRepository.findById(id);
    }

    public Reserva guardarReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public Optional<Reserva> actualizarReserva(Integer id, Reserva reserva) {
        if (reservaRepository.existsById(id)){
            reserva.setIdReserva(id);
            return Optional.of(reservaRepository.save(reserva));
        }
        return Optional.empty();
    }

    public boolean eliminarReserva(Integer id) {
        if(reservaRepository.existsById(id)){
            reservaRepository.deleteById(id);
            return true;
        }
        return false;


    }
}

