package com.springboot.apirest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/register", "/verify_session").permitAll() // Permitir acceso sin autenticación
                .antMatchers("/profile/**").hasAnyRole("USER", "ADMIN_CLUB", "ADMIN_SUPERIOR") // Acceso para los roles específicos
                .antMatchers("/admin/**").hasRole("ADMIN_SUPERIOR") // Solo admin superior
                .anyRequest().authenticated()
                .and()
                .formLogin().disable() // Deshabilitar el formulario de login de Spring Security
                .logout()
                .permitAll()
                .logoutUrl("/logout") // URL de logout
                .logoutSuccessUrl("/login"); // Redirigir a login al cerrar sesión

         */
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll() // Permitir acceso a todas las rutas sin autenticación
                .and()
                .formLogin().disable() // Deshabilitar el formulario de login de Spring Security
                .logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Configuración de usuario en memoria para pruebas (en un caso real sería desde una base de datos)
        /*
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("{noop}password") // {noop} es para que no se cifre la contraseña en pruebas
                .roles("USER")
                .and()
                .withUser("admin")
                .password("{noop}admin")
                .roles("ADMIN_CLUB")
                .and()
                .withUser("superadmin")
                .password("{noop}superadmin")
                .roles("ADMIN_SUPERIOR");
        */
    }
}

