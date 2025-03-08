package com.springboot.apirest.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pistas")
@Getter @Setter @NoArgsConstructor
public class Pista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPista;

    @Column(nullable = false, length = 50)
    private String nombrePista;

    @Column(length = 100)
    private String ubicacion;

    @Column(length = 50)
    private String tipoPista;

    @ManyToOne
    @JoinColumn(name = "id_club")
    private Club club;

    @Column(nullable = false, length = 20)
    private String estado;
}