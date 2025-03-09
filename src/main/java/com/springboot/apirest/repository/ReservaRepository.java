package com.springboot.apirest.repository;

import com.springboot.apirest.dao.Reserva;
import com.springboot.apirest.dao.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    //@Query("SELECT r FROM com.springboot.apirest.dao.Reserva r WHERE r.idUsuario = :idUsuario AND (r.fechaReserva > :fechaActual OR (r.fechaReserva = :fechaActual AND r.horaFin > :horaActual))")
    //List<Reserva> obtenerReservasDisponibles(Integer idUsuario, LocalDate fechaActual, LocalTime horaActual);

    List<Reserva> findByUsuarioAndFechaReservaGreaterThanEqualOrFechaReservaAndHoraFinGreaterThan(
            Usuario usuario, Date fechaReserva, Date fechaReserva2, String horaFin
    );

    List<Reserva> findByUsuarioAndFechaReservaGreaterThanEqualAndHoraInicioGreaterThanEqual(
            Usuario usuario, Date fechaReserva, String horaFin
    );


}
