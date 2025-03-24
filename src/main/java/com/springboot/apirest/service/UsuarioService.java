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
import java.util.Arrays;
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


    /**
     * GET ALL USERS
     */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }



    /**
     * GET USER
     */
    public Optional<Usuario> obtenerUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id);
    }


    /**
     *  POST USER
     */
    public Usuario guardarUsuario(Usuario usuario) {
        usuario.setFechaRegistro(new Date(System.currentTimeMillis()));
        if (usuario.getClub() != null){
            //guardar club
            Club club = new Club();
            club.setNombre(usuario.getClub());
            club.setTelefono(usuario.getTelefono());
            club.setUbicacion(usuario.getUbicacion());
            Integer idClub = clubRepository.save(club).getIdClub();
            //guardar pista/s
            guardarPista(usuario, idClub);
            usuario.setPistas(usuario.getNombrePistas().stream().map(PistaDto::getNombre).collect(Collectors.joining(", ")));
        }
        return usuarioRepository.save(usuario);
    }


    /**
     *  UPDATE USER
     */
    public boolean updateUsuario(Usuario user) {
        Usuario user1 = usuarioRepository.findByEmail(user.getEmail()).get();
        String tmp = user1.getPistas();
        user1.setPistas(tmp+", "+user.getNuevaPista());
        usuarioRepository.save(user1);
        return true;
    }



    public boolean borrarUsuarioPista(Usuario user) {
        Usuario user1 = usuarioRepository.findByEmail(user.getEmail()).get();
        // Expresión regular para eliminar el literal asegurando el correcto manejo de comas
        //String regex = "\\s*,?\\s*" + user.getNuevaPista() + "\\s*,?";
        // Reemplazo asegurando que no queden comas sobrantes
        //String result = user1.getPistas().replaceAll(regex, ", ").replaceAll("^,\\s*|,\\s*$", null).trim();
        List<String> list = Arrays.asList( user1.getPistas().split(","));
        list.remove(user.getNuevaPista());
        user1.setPistas(String.join(", ", list));
        usuarioRepository.save(user1);
        return true;
    }



    public boolean editarUsuarioPista(Usuario user) {
        //Editar en tabla de usuarios, el nombre de la pista
        Usuario user1 = usuarioRepository.findByEmail(user.getEmail()).get();
        // Expresión regular para eliminar el literal asegurando el correcto manejo de comas
        //String regex = "\\s*,?\\s*" + user.getNuevaPista() + "\\s*,?";
        // Reemplazo asegurando que no queden comas sobrantes
        //String result = user1.getPistas().replace(user.getNombre(),user.getNuevaPista());
        List<String> list = Arrays.asList( user1.getPistas().split(","));
        list.remove(user.getNombre());
        list.add(user.getNuevaPista());
        user1.setPistas(String.join(", ", list));
        usuarioRepository.save(user1);

        //Editar el nombre de la pista en la tabla pistas
        Pista pista = new Pista();
        pista.setNombrePista(user.getNombre());//actual
        pista.setNuevoNombre(user.getNuevaPista());//nuevo nombre
        ArrayList<Pista> pista1 = (ArrayList<Pista>) pistaRepository.findByNombrePista(pista.getNombrePista());
        pista1.forEach(p ->{
            p.setNombrePista(pista.getNuevoNombre());
            pistaRepository.save(p);
        });
        return true;
    }


    /**
     *  DELETE USER
     */
    public void eliminarUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }







    private void guardarPista(Usuario usuario, Integer idClub) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime fechaActual = LocalDateTime.now();

        Club club = clubRepository.getOne(idClub); // Obtener el club una sola vez
        List<Pista> listaDePistas = new ArrayList<>();
        List<String> horarios = Arrays.asList("17:00:00", "18:30:00", "20:00:00", "21:30:00", "23:00:00");

        for (PistaDto pistaUsuario : usuario.getNombrePistas()) { // Itera directamente sobre las pistas
            for (int j = 0; j < 8; j++) {
                String fechaBase = fechaActual.plusDays(j).format(formatter);
                for (String hora : horarios) {
                    Pista pista = new Pista();
                    pista.setClub(club);
                    pista.setNombrePista(pistaUsuario.getNombre());
                    pista.setUbicacion(usuario.getUbicacion());
                    pista.setTipoPista(pistaUsuario.getTipo());
                    pista.setEstado("Disponible");
                    pista.setFechaHora(fechaBase + " " + hora);
                    listaDePistas.add(pista);
                }
            }
        }

        pistaRepository.saveAll(listaDePistas); // Guardar todas las pistas en una sola operación
    }








}
