package com.springboot.apirest.service;

import com.springboot.apirest.dao.Club;
import com.springboot.apirest.dao.Pista;
import com.springboot.apirest.repository.ClubRepository;
import com.springboot.apirest.repository.PistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;

@Service
public class ClubService {
    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private PistaRepository pistaRepository;

    public List<Club> getAllClubes() {
        return clubRepository.findAll();
    }

    public Optional<Club> getClubById(Integer id) {
        return clubRepository.findById(id);
    }

    public Club createClub(Club club) {
        return clubRepository.save(club);
    }

    public Optional<Club> updateClub(Integer id, Club club) {
        if (clubRepository.existsById(id)) {
            club.setIdClub(id);
            return Optional.of(clubRepository.save(club));
        }
        return Optional.empty();
    }

    public boolean deleteClub(Integer id) {
        if (clubRepository.existsById(id)) {
            clubRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Pista> getClubByNombre(String nombre) {
        List<Pista> reservas = new ArrayList<>();
        Club club = clubRepository.findByNombre(nombre);
        List<Pista> pistaList = pistaRepository.findByClub_IdClub(club.getIdClub());
        pistaList.forEach(pista -> {
            if (pista.getEstado().equals("No Disponible")){
                reservas.add(pista);
            }
        });
        return reservas;
    }
}
