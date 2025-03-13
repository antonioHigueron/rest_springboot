package com.springboot.apirest.service;

import com.springboot.apirest.dao.Club;
import com.springboot.apirest.dao.Usuario;
import com.springboot.apirest.repository.ClubRepository;
import com.springboot.apirest.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ClubRepository clubRepository;


    public UsuarioService(UsuarioRepository usuarioRepository, ClubRepository clubRepository) {
        this.usuarioRepository = usuarioRepository;
        this.clubRepository = clubRepository;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        //guardar club
        Club club = new Club();
        club.setNombre(usuario.getClub());
        club.setTelefono(usuario.getTelefono());
        club.setUbicacion(usuario.getUbicacion());
        String idClub = clubRepository.save(club).getIdClub().toString();
        //guardar pista/s
        

        usuario.setFechaRegistro(new Date(System.currentTimeMillis()));
        usuario.setPistas(String.join(",", usuario.getNombrePistas()));
        /*
        Usuario usuario1 = new Usuario();
        usuario1.setNombre(usuario.getNombre());
        usuario1.setApellidos(usuario.getApellidos());
        usuario1.setEmail(usuario.getEmail());
        usuario1.setContrasena(usuario.getContrasena());
        usuario1.setRol(usuario.getRol());
        usuario1.setClub(usuario.getClub());
        usuario1.setTelefono(usuario.getTelefono());
        usuario1.setFechaRegistro(new Date(System.currentTimeMillis()));
        usuario1.setPistas(String.join(", ", usuario.getNombrePistas()));
        */
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }
}
