package com.springboot.apirest.service;

import com.springboot.apirest.dao.HistoricoReserva;
import com.springboot.apirest.dao.Reserva;
import com.springboot.apirest.repository.HistoricoReservaRepository;
import com.springboot.apirest.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoricoReservaService {
    @Autowired
    private HistoricoReservaRepository historicoReservasRepository;

    public List<HistoricoReserva> getAllHistoricoReservas() {
        return historicoReservasRepository.findAll();
    }

    public Optional<HistoricoReserva> getHistoricoReservaById(Integer id) {
        return historicoReservasRepository.findById(id);
    }

    public HistoricoReserva createHistoricoReserva(HistoricoReserva historicoReserva) {
        return historicoReservasRepository.save(historicoReserva);
    }

    public Optional<HistoricoReserva> updateHistoricoReserva(Integer id, HistoricoReserva historicoReserva) {
        if (historicoReservasRepository.existsById(id)) {
            historicoReserva.setIdHistorico(id);
            return Optional.of(historicoReservasRepository.save(historicoReserva));
        }
        return Optional.empty();
    }

    public boolean deleteHistoricoReserva(Integer id) {
        if (historicoReservasRepository.existsById(id)) {
            historicoReservasRepository.deleteById(id);
            return true;
        }
        return false;
    }
}