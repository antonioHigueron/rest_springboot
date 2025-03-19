package com.springboot.apirest.repository;

import com.springboot.apirest.dao.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Integer> {
    Club findByNombre(String nombre);
}
