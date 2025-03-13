package com.springboot.apirest.dao;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "reservas")
@Getter
@Setter
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idReserva;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_pista")
    private Pista pista;

    private Date fechaReserva;
    private String horaInicio;
    private String horaFin;
    private String estado;
    private Integer contadorModificaciones;
}
