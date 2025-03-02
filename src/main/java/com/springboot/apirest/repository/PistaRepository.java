package com.springboot.apirest.repository;

import com.springboot.apirest.dao.Pista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PistaRepository extends JpaRepository<Pista, Integer> {}
