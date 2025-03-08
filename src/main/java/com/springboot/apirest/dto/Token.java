package com.springboot.apirest.dto;

import com.springboot.apirest.dao.Usuario;

public class Token {

    private String token;
    private Usuario usuario;

    public Token(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
