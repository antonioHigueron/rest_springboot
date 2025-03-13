package com.springboot.apirest.dao;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "pistas")
@Getter
@Setter
public class Pista {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idPista;
    private String nombrePista;
    private String ubicacion;
    private String tipoPista;
    private String estado;
    private String fechaHora;

    @ManyToOne
    @JoinColumn(name = "id_club")
    private Club club;
}
