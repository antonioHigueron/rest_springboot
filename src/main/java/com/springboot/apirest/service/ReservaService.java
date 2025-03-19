package com.springboot.apirest.service;

import com.google.zxing.WriterException;
import com.springboot.apirest.dao.Reserva;
import com.springboot.apirest.dao.Usuario;
import com.springboot.apirest.repository.PistaRepository;
import com.springboot.apirest.repository.ReservaRepository;
import com.springboot.apirest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
        pistaService.actualizarEstado(reserva.getPista().getIdPista(),"No Disponible");
        try {
            emailService.enviarCorreo("acobosscabello@gmail.com","Reserva padel", "Tiene confirmada reserva de pista de padel el "+reserva.getFechaReserva()+" a las "
                    +reserva.getHoraInicio() +" en el club "+reserva.getPista().getClub().getNombre() +" en la pista: "+reserva.getPista().getNombrePista()+
                    " Gracias por todo.", "+34"+reserva.getPista().getClub().getTelefono());
            } catch (Exception e) {
            throw new RuntimeException(e);
        }
        reserva.setHoraFin(reserva.getHoraInicio().equals("17:00")?"18:30":
                reserva.getHoraInicio().equals("18:30")?"20:00":
                reserva.getHoraInicio().equals("20:00")?"21:30":
                reserva.getHoraInicio().equals("21:30")?"23:00":"00:00");
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

