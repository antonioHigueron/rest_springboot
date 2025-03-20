package com.springboot.apirest.service;

import com.springboot.apirest.dao.Reserva;
import com.springboot.apirest.dao.Usuario;
import com.springboot.apirest.repository.PistaRepository;
import com.springboot.apirest.repository.ReservaRepository;
import com.springboot.apirest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PistaService pistaService;
    @Autowired
    private EmailService emailService;

    public ReservaService(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository, PistaRepository pistaRepository, PistaService pistaService, EmailService emailService) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.pistaService = pistaService;
        this.emailService = emailService;
    }

    /**
     *
     * @return
     */
    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<List<Optional<Reserva>>> obtenerReservaPorId(Integer id) {
        return Optional.of(List.of(reservaRepository.findById(id))) ;
    }

    /**
     *
     * @param idUsuario
     * @return
     */
    public List<Reserva> obtenerReservasDisponibles(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        //LocalDate fechaActual = LocalDate.now();
        Date fechaActual = Date.valueOf(LocalDate.now());
        //LocalTime horaActual = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        String horaActual = LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString();

        //return reservaRepository.findByUsuarioAndFechaReservaGreaterThanEqualAndHoraInicioGreaterThanEqual(usuario, fechaActual, horaActual);
        List<Reserva> list =  reservaRepository.findReservasFuturas(usuario, fechaActual, horaActual);

        for (Reserva reserva : list) {
            if (reserva.getRestriccionNivel() != null){
                reserva.setRestringir("Si");
            }else{
                reserva.setRestringir("No");
                reserva.setRestriccionNivel(0);
            }
            if (reserva != null){
                String jugadorList = pistaService.getPistaById(reserva.getPista().getIdPista()).get().getJugadorEmail();
                if ( jugadorList != null && jugadorList.split(",").length != 4){
                    reserva.setEstado("Abierta");
                }
            }

        }
        //Si alguno de los atributos es null, no se devuelve la reserva
        List<Reserva> reservasFiltradas = list.stream()
                .filter(reserva -> {
                    for (Field field : reserva.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        try {
                            if (field.get(reserva) == null) return false;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());

        return reservasFiltradas;
    }

    /**
     *
     * @param reserva
     * @return
     */
    public Reserva guardarReserva(Reserva reserva) {
        pistaService.actualizarEstado(reserva.getPista().getIdPista(),"No Disponible", reserva.getUsuario().getEmail());
        if (reserva.getRestringir().equals("Si")){
            reserva.setRestriccionNivel(reserva.getUsuario().getNivel());
        }else{
            reserva.setRestriccionNivel(0);
        }

        try {
            emailService.enviarCorreo(reserva.getUsuario().getEmail(),"Reserva padel", "Tiene confirmada reserva de pista de padel el: "+reserva.getFechaReserva()+" a las "
                    +reserva.getHoraInicio() +" en el club: "+reserva.getPista().getClub().getNombre() +"\r\n y en la pista: "+reserva.getPista().getNombrePista()+
                    " Gracias por todo.", "+34"+reserva.getPista().getClub().getTelefono());
            } catch (Exception e) {
            throw new RuntimeException(e);
        }
        reserva.setResultado("");
        return reservaRepository.save(reserva);
    }

    /**
     *
     * @param id
     * @param reserva
     * @return
     */
    public Optional<Reserva> actualizarReserva(Integer id, Reserva reserva) {
        if (reservaRepository.existsById(id)){
            reserva.setIdReserva(id);
            return Optional.of(reservaRepository.save(reserva));
        }
        return Optional.empty();
    }

    public Optional<Reserva> actualizarReservaResultado(Integer id, Map<String, String> resultado) {
        if (reservaRepository.existsById(id)){
            Reserva reserva = reservaRepository.findById(id).get();
            reserva.setResultado(resultado.get("resultado"));

            Usuario usuario= usuarioRepository.findById(reserva.getUsuario().getIdUsuario()).get();
            int nivel = usuario.getNivel();
            nivel += evaluarResultado(resultado.get("resultado"));
            nivel = nivel < 1 ? nivel=1:nivel;
            nivel = nivel > 7 ? nivel=7:nivel;
            usuario.setNivel(nivel);
            usuarioRepository.save(usuario);
            return Optional.of(reservaRepository.save(reserva));


        }
        return Optional.empty();
    }

    private int evaluarResultado(String resultado) {
        int puntosA = 0;
        int puntosB = 0;

        for (String set : resultado.split(",")) {
            String[] puntajes = set.trim().split("-"); // Separar por "-"
            int p1 = Integer.parseInt(puntajes[0]);   // Primer número
            int p2 = Integer.parseInt(puntajes[1]);   // Segundo número

            if (p1 > p2) {
                puntosA++;  // Sumar victoria para el primer jugador
            } else {
                puntosB++;  // Sumar victoria para el segundo jugador
            }
        }

        return (puntosA > puntosB) ? 1 : -1;
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean eliminarReserva(Integer id) {
        if(reservaRepository.existsById(id)){
            reservaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean borrarReservasAnteriores() throws ParseException {
        int eliminadas = reservaRepository.borrarReservasAnteriores();
        if (eliminadas > 0){
            return true;
        }else{
            return false;
        }
    }



}

