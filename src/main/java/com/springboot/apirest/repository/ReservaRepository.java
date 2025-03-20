package com.springboot.apirest.repository;

import com.springboot.apirest.dao.Reserva;
import com.springboot.apirest.dao.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    //@Query("SELECT r FROM com.springboot.apirest.dao.Reserva r WHERE r.idUsuario = :idUsuario AND (r.fechaReserva > :fechaActual OR (r.fechaReserva = :fechaActual AND r.horaFin > :horaActual))")
    //List<Reserva> obtenerReservasDisponibles(Integer idUsuario, LocalDate fechaActual, LocalTime horaActual);

    List<Reserva> findByUsuarioAndFechaReservaGreaterThanEqualOrFechaReservaAndHoraFinGreaterThan(
            Usuario usuario, Date fechaReserva, Date fechaReserva2, String horaFin
    );

    List<Reserva> findByUsuarioAndFechaReservaGreaterThanEqualAndHoraInicioGreaterThanEqual(
            Usuario usuario, Date fechaReserva, String horaFin
    );

    @Query("SELECT r FROM Reserva r WHERE r.usuario = :usuario AND " +
            "(r.fechaReserva > :fechaActual OR " +
            "(r.fechaReserva = :fechaActual AND r.horaInicio >= :horaInicio))")
    List<Reserva> findReservasFuturas(@Param("usuario") Usuario usuario,
                                      @Param("fechaActual") Date fechaActual,
                                      @Param("horaInicio") String horaInicio);


    /**
     * eliminar reservas si son de dias anteriores al dia actual
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Reserva r WHERE r.fechaReserva < CURRENT_DATE")
    int borrarReservasAnteriores();


    List<Reserva> findByPista_IdPista(Integer idPista);


}
