package com.springboot.apirest.repository;

import com.springboot.apirest.dao.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {}
