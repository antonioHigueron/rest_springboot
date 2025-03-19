package com.springboot.apirest.dao;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;
import com.springboot.apirest.dto.PistaDto;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private Date fechaRegistro;
    private String contrasena;
    private String rol;
    private String club;
    //esto sirve para que no se mapee con la entidad de basedatos a la que se vincula
    @Transient
    private List<PistaDto> nombrePistas;
    @Transient
    private String nuevaPista;
    private String pistas;
    private String ubicacion;


}
