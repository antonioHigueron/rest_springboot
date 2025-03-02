package com.springboot.apirest.service;

import com.springboot.apirest.dao.Club;
import com.springboot.apirest.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClubService {
    @Autowired
    private ClubRepository clubRepository;

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
}
