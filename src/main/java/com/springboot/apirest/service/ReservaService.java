package com.springboot.apirest.service;

import com.springboot.apirest.dao.Reserva;
import com.springboot.apirest.dao.Usuario;
import com.springboot.apirest.repository.PistaRepository;
import com.springboot.apirest.repository.ReservaRepository;
import com.springboot.apirest.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PistaService pistaService;

    public ReservaService(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository, PistaRepository pistaRepository, PistaService pistaService) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.pistaService = pistaService;
    }

    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    public Optional<List<Optional<Reserva>>> obtenerReservaPorId(Integer id) {
        return Optional.of(List.of(reservaRepository.findById(id))) ;
    }

    public List<Reserva> obtenerReservasDisponibles(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        //LocalDate fechaActual = LocalDate.now();
        Date fechaActual = Date.valueOf(LocalDate.now());
        //LocalTime horaActual = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        String horaActual = LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString();

       /* return reservaRepository.findByUsuarioAndFechaReservaGreaterThanEqualOrFechaReservaAndHoraFinGreaterThan(
                usuario, fechaActual, fechaActual, horaActual
        );*/
        return reservaRepository.findByUsuarioAndFechaReservaGreaterThanEqualAndHoraInicioGreaterThanEqual(
                usuario, fechaActual, horaActual
        );
    }


    public Reserva guardarReserva(Reserva reserva) {
        pistaService.actualizarEstado(reserva.getPista().getIdPista(),"No Disponible");
        return reservaRepository.save(reserva);
    }

    public Optional<Reserva> actualizarReserva(Integer id, Reserva reserva) {
        if (reservaRepository.existsById(id)){
            reserva.setIdReserva(id);
            return Optional.of(reservaRepository.save(reserva));
        }
        return Optional.empty();
    }

    public boolean eliminarReserva(Integer id) {
        if(reservaRepository.existsById(id)){
            reservaRepository.deleteById(id);
            return true;
        }
        return false;


    }
}

