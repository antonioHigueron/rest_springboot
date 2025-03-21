package com.springboot.apirest.service;


import com.springboot.apirest.dao.Pista;

import com.springboot.apirest.dao.Reserva;
import com.springboot.apirest.dao.Usuario;
import com.springboot.apirest.repository.PistaRepository;
import com.springboot.apirest.repository.ReservaRepository;
import com.springboot.apirest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PistaService {
    @Autowired
    private PistaRepository pistaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    public List<Pista> getAllPistas() {
        return pistaRepository.findAll();
    }

    public Optional<Pista> getPistaById(Integer id) {
        return pistaRepository.findById(id);
    }

    public List<Pista> getPistaByIdClub(Integer id, String fecha, String email) {
        List<Pista> pistas = pistaRepository.findByClub_IdClub(id);
        List<Pista> pistaList = new ArrayList<>();
        List<Pista> pistaListTmp = new ArrayList<>();
        //pistas.removeIf(pista -> (pista.getEstado().equals("No Disponible") && pista.getFechaHora().equals("2024-03-09 18:30:00")) );
        //pistas.removeIf(pista -> pista.getFechaHora().equals("2024-03-09 18:30:00") );
        for (int i = 0; i < pistas.size(); i++) {
            // Extraer fecha y hora
            String fechaStr = pistas.get(i).getFechaHora().substring(0, 10);
            String horaStr = pistas.get(i).getFechaHora().substring(11, 19);
            // Obtener la fecha y hora actuales
            LocalDate fechaHoy = LocalDate.now();
            LocalTime horaActual = LocalTime.now();
            // Convertir a objetos LocalDate y LocalTime
            LocalDate fechaReserva = LocalDate.parse(fechaStr);
            LocalTime horaReserva = LocalTime.parse(horaStr);
            //if (pistas.get(i).getEstado().equals("No Disponible") && pistas.get(i).getFechaHora().equals("2024-03-09 18:30:00") ){
            if (fechaReserva.toString().equals(fecha) && horaReserva.isAfter(horaActual)){
                pistaList.add(pistas.get(i));
                //para que un mismo usuario no pueda reservar la misma pista una y otra vez
                if (pistas.get(i).getEstado().equals("No Disponible") || (pistas.get(i).getFechaHora().substring(0,10).equals(fecha) && (pistas.get(i).getJugadorEmail() != null && pistas.get(i).getJugadorEmail().contains(email))) ){
                    pistaList.remove(pistas.get(i));
                }
            }
        }
        pistaListTmp = new ArrayList<>(pistaList);
        //filtrar para que si la reserva esta restringida a un nivel, no se muestre a usuarios de otros niveles
        Usuario user = usuarioRepository.findByEmail(email).get();
        int nivel = user.getNivel();
        for (Pista pista : pistaList) {
            List<Reserva> reserva = reservaRepository.findByPista_IdPista(pista.getIdPista());
            for (Reserva reserva1 : reserva) {
                if (reserva1 != null){
                    if (reserva1.getRestriccionNivel() != 0 && reserva1.getRestriccionNivel() != nivel ){
                        pistaListTmp.remove(pista);
                    }
                }
            }


        }
        return pistaListTmp;
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