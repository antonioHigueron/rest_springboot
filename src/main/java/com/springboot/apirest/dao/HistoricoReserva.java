package com.springboot.apirest.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "historico_reservas")
@Getter
@Setter
public class HistoricoReserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistorico;

    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_pista")
    private Pista pista;

    private Date fechaReserva;
    private String horaInicio;
    private String horaFin;
    private String estado;
    private Integer contadorModificaciones;
    private Date fechaModificacion;
}
