package com.springboot.apirest.repository;

import com.springboot.apirest.dao.Pista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PistaRepository extends JpaRepository<Pista, Integer> {
    //esto es para buscar dentro del atributo Club que es un objeto que se encuentra en la entidad Pista, el atributo idClub del obj Club
    List<Pista> findByClub_IdClub(Integer id);
}
