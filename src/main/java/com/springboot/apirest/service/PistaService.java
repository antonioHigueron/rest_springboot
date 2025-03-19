package com.springboot.apirest.service;


import com.springboot.apirest.dao.Pista;

import com.springboot.apirest.dao.Usuario;
import com.springboot.apirest.repository.PistaRepository;
import com.springboot.apirest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@Service
public class PistaService {
    @Autowired
    private PistaRepository pistaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Pista> getAllPistas() {
        return pistaRepository.findAll();
    }

    public Optional<Pista> getPistaById(Integer id) {
        return pistaRepository.findById(id);
    }

    public List<Pista> getPistaByIdClub(Integer id, String fecha, String hora) {
        List<Pista> pistas = pistaRepository.findByClub_IdClub(id);
        List<Pista> pistaList = new ArrayList<>();
        //pistas.removeIf(pista -> (pista.getEstado().equals("No Disponible") && pista.getFechaHora().equals("2024-03-09 18:30:00")) );
        //pistas.removeIf(pista -> pista.getFechaHora().equals("2024-03-09 18:30:00") );
        for (int i = 0; i < pistas.size(); i++) {
            //if (pistas.get(i).getEstado().equals("No Disponible") && pistas.get(i).getFechaHora().equals("2024-03-09 18:30:00") ){
            if (pistas.get(i).getFechaHora().substring(0,10).equals(fecha) ){
                pistaList.add(pistas.get(i));
                if (pistas.get(i).getEstado().equals("No Disponible") && pistas.get(i).getFechaHora().substring(0,10).equals(fecha) ){
                    pistaList.remove(pistas.get(i));
                }
            }
        }
        return pistaList;
    }

    public Pista createPista(Pista pista) {
        return pistaRepository.save(pista);
    }

    public boolean updatePista(Pista pista) {
        ArrayList<Pista> pista1 = (ArrayList<Pista>) pistaRepository.findByNombrePista(pista.getNombrePista());
            pista1.forEach(p ->{
                p.setNombrePista(pista.getNuevoNombre());
                pistaRepository.save(p);
            });

            Usuario usuario = usuarioRepository.findByEmail(pista.getEmail()).get();
            String userPistas = usuario.getPistas();
            String cambio = userPistas.replace(pista.getNombrePista(), pista.getNuevoNombre());
            usuario.setPistas(cambio);
            usuarioRepository.save(usuario);
            return true;
    }

    public boolean deletePista(String nombre, String email) {
        ArrayList<Pista> pista1 = (ArrayList<Pista>) pistaRepository.findByNombrePista(nombre);
        pista1.forEach(p ->{
            pistaRepository.deleteAll(pista1);//deleteByNombrePista(nombre);
        });

        Usuario usuario = usuarioRepository.findByEmail(email).get();
        String userPistas = usuario.getPistas();
        String cambio = userPistas.replace(", "+nombre, "");
        usuario.setPistas(cambio);
        usuarioRepository.save(usuario);


        return true;
    }

    public void actualizarEstado(Integer idPista, String nuevoEstado, String email) {
        Optional<Pista> pistaOpt = pistaRepository.findById(idPista);

        if (pistaOpt.isPresent()) {
            Pista pista = pistaOpt.get();
            pista.setJugadorEmail(pista.getJugadorEmail() != null ? pista.getJugadorEmail()+", "+email : email);
            if (pista.getJugadorEmail().split(",").length == 4){
                pista.setEstado(nuevoEstado);
            }
            pistaRepository.save(pista);  // Guardar la pista actualizada
        } else {
            throw new RuntimeException("Pista no encontrada con ID: " + idPista);
        }
    }



}