package com.springboot.apirest.service;


import com.springboot.apirest.model.Usuario;
//import com.springboot.auth.repository.UsuarioRepository;
//import com.springboot.auth.security.JwtUtil;
import  com.springboot.apirest.util.JwtUtil;
import com.springboot.apirest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String login(String email, String password) throws Exception {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Verificar la contraseña encriptada
            if (passwordEncoder.matches(password, usuario.getContrasena())) {
                return jwtUtil.generarToken(email); // Generar JWT
            } else {
                throw new Exception("Credenciales incorrectas");
            }
        } else {
            throw new Exception("Usuario no encontrado");
        }
    }
}
