package com.springboot.apirest.service;


import com.springboot.apirest.dao.Pista;

import com.springboot.apirest.repository.PistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PistaService {
    @Autowired
    private PistaRepository pistaRepository;

    public List<Pista> getAllPistas() {
        return pistaRepository.findAll();
    }

    public Optional<Pista> getPistaById(Integer id) {
        return pistaRepository.findById(id);
    }

    public List<Pista> getPistaByIdClub(Integer id, String fecha, String hora) {
        List<Pista> pistas = pistaRepository.findByClub_IdClub(id);
        List<Pista> pistaList = new ArrayList<>();
        //pistas.removeIf(pista -> (pista.getEstado().equals("No Disponible") && pista.getFechaHora().equals("2024-03-09 18:30:00")) );
        //pistas.removeIf(pista -> pista.getFechaHora().equals("2024-03-09 18:30:00") );
        for (int i = 0; i < pistas.size(); i++) {
            //if (pistas.get(i).getEstado().equals("No Disponible") && pistas.get(i).getFechaHora().equals("2024-03-09 18:30:00") ){
            if (pistas.get(i).getFechaHora().substring(0,10).equals(fecha) ){
                pistaList.add(pistas.get(i));
                if (pistas.get(i).getEstado().equals("No Disponible") && pistas.get(i).getFechaHora().substring(0,10).equals(fecha) ){
                    pistaList.remove(pistas.get(i));
                }
            }
        }
        return pistaList;
    }

    public Pista createPista(Pista pista) {
        return pistaRepository.save(pista);
    }

    public Optional<Pista> updatePista(Integer id, Pista pista) {
        if (pistaRepository.existsById(id)) {
            pista.setIdPista(id);
            return Optional.of(pistaRepository.save(pista));
        }
        return Optional.empty();
    }

    public boolean deletePista(Integer id) {
        if (pistaRepository.existsById(id)) {
            pistaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Pista actualizarEstado(Integer idPista, String nuevoEstado) {
        Optional<Pista> pistaOpt = pistaRepository.findById(idPista);

        if (pistaOpt.isPresent()) {
            Pista pista = pistaOpt.get();
            pista.setEstado(nuevoEstado);  // Actualizar el estado
            return pistaRepository.save(pista);  // Guardar la pista actualizada
        } else {
            throw new RuntimeException("Pista no encontrada con ID: " + idPista);
        }
    }



}