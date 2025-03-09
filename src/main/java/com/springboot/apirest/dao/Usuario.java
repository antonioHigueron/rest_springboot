package com.springboot.apirest.dao;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private Date fechaRegistro;
    private String contrasena;
}
