package com.springboot.apirest.repository;

import com.springboot.apirest.dao.HistoricoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoReservaRepository extends JpaRepository<HistoricoReserva, Integer> {}
