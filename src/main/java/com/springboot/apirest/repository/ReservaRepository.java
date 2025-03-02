package com.springboot.apirest.repository;

import com.springboot.apirest.dao.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {}
