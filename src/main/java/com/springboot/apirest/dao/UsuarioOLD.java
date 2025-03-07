package com.springboot.apirest.dao;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "usuariosOLD")
@Getter
@Setter
public class UsuarioOLD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private Date fechaRegistro;
    private String contrasena;
}
