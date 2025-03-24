package com.springboot.apirest.repository;

import com.springboot.apirest.dao.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);

    Usuario findByClub(String nombre);
}
