package com.springboot.apirest.service;


import com.springboot.apirest.dao.Usuario;
//import com.springboot.auth.repository.UsuarioRepository;
//import com.springboot.auth.security.JwtUtil;
import com.springboot.apirest.dto.Token;
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

    public Token login(String email, String password) throws Exception {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        final Usuario usuario;
        if (usuarioOpt.isPresent()) {
            usuario = usuarioOpt.get();
            String passwordEncrypted = passwordEncoder.encode(usuario.getContrasena());
            // Verificar la contraseña encriptada
            if (passwordEncoder.matches(password, passwordEncrypted)) {
                //return new Object(){String token3=jwtUtil.generarToken(email);String usuario2= usuario.getNombre();};   // Generar JWT, necesito una clase, porque si es serializable para mandarla por http
                return  new Token(jwtUtil.generarToken(email),usuario);  // Generar JWT, necesito una clase, porque si es serializable para mandarla por http
            } else {
                throw new Exception("Credenciales incorrectas");
            }
        } else {
            throw new Exception("Usuario no encontrado");
        }
    }
}
