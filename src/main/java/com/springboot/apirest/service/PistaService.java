package com.springboot.apirest.service;


import com.springboot.apirest.dao.Pista;

import com.springboot.apirest.repository.PistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}