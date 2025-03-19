package com.springboot.apirest.service;

import com.springboot.apirest.dao.Club;
import com.springboot.apirest.dto.PistaDto;
import com.springboot.apirest.dao.Pista;
import com.springboot.apirest.dao.Usuario;
import com.springboot.apirest.repository.ClubRepository;
import com.springboot.apirest.repository.PistaRepository;
import com.springboot.apirest.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ClubRepository clubRepository;
    private final PistaRepository pistaRepository;



    public UsuarioService(UsuarioRepository usuarioRepository, ClubRepository clubRepository, PistaRepository pistaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.clubRepository = clubRepository;
        this.pistaRepository = pistaRepository;
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
        Integer idClub = clubRepository.save(club).getIdClub();
        //guardar pista/s
        guardarPista(usuario, idClub);
        usuario.setFechaRegistro(new Date(System.currentTimeMillis()));
        usuario.setPistas(usuario.getNombrePistas().stream().map(PistaDto::getNombre).collect(Collectors.joining(",")));
        return usuarioRepository.save(usuario);
    }

    private void guardarPista(Usuario usuario, Integer idClub) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime fechaActual = LocalDateTime.now();
            for (int i = 0; i < usuario.getNombrePistas().size(); i++) {
                for (int j = 0; j < 8; j++) {
                    Pista pista = new Pista();
                    pista.setClub(clubRepository.getOne(idClub));
                    pista.setNombrePista(usuario.getNombrePistas().get(i).getNombre());
                    pista.setUbicacion(usuario.getUbicacion());
                    pista.setTipoPista(usuario.getNombrePistas().get(i).getTipo());
                    pista.setEstado("Disponible");
                    pista.setFechaHora(fechaActual.plusDays(j).format(formatter) + " 17:00:00");
                    pistaRepository.save(pista);
                    Pista pista2 = new Pista();
                    pista2.setClub(clubRepository.getOne(idClub));
                    pista2.setNombrePista(usuario.getNombrePistas().get(i).getNombre());
                    pista2.setUbicacion(usuario.getUbicacion());
                    pista2.setTipoPista(usuario.getNombrePistas().get(i).getTipo());
                    pista2.setEstado("Disponible");
                    pista2.setFechaHora(fechaActual.plusDays(j).format(formatter) + " 18:30:00");
                    pistaRepository.save(pista2);
                    Pista pista3 = new Pista();
                    pista3.setClub(clubRepository.getOne(idClub));
                    pista3.setNombrePista(usuario.getNombrePistas().get(i).getNombre());
                    pista3.setUbicacion(usuario.getUbicacion());
                    pista3.setTipoPista(usuario.getNombrePistas().get(i).getTipo());
                    pista3.setEstado("Disponible");
                    pista3.setFechaHora(fechaActual.plusDays(j).format(formatter) + " 20:00:00");
                    pistaRepository.save(pista3);
                    Pista pista4 = new Pista();
                    pista4.setClub(clubRepository.getOne(idClub));
                    pista4.setNombrePista(usuario.getNombrePistas().get(i).getNombre());
                    pista4.setUbicacion(usuario.getUbicacion());
                    pista4.setTipoPista(usuario.getNombrePistas().get(i).getTipo());
                    pista4.setEstado("Disponible");
                    pista4.setFechaHora(fechaActual.plusDays(j).format(formatter) + " 21:30:00");
                    pistaRepository.save(pista4);
                    Pista pista5 = new Pista();
                    pista5.setClub(clubRepository.getOne(idClub));
                    pista5.setNombrePista(usuario.getNombrePistas().get(i).getNombre());
                    pista5.setUbicacion(usuario.getUbicacion());
                    pista5.setTipoPista(usuario.getNombrePistas().get(i).getTipo());
                    pista5.setEstado("Disponible");
                    pista5.setFechaHora(fechaActual.plusDays(j).format(formatter) + " 23:00:00");
                    pistaRepository.save(pista5);
                }
            }
    }

    public void eliminarUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }


    public boolean updateUsuario(Usuario user) {
        Usuario user1 = usuarioRepository.findByEmail(user.getEmail()).get();
        String tmp = user1.getPistas();
        user1.setPistas(tmp+", "+user.getNuevaPista());
        usuarioRepository.save(user1);
        return true;
    }
}
