package com.example.microserviciousuarios.config.secutiry

import com.example.microserviciousuarios.config.secutiry.jwt.JwtAuthenticationFilter
import com.example.microserviciousuarios.config.secutiry.jwt.JwtAuthorizationFilter
import com.example.microserviciousuarios.config.secutiry.jwt.JwtTokenUtil
import com.example.microserviciousuarios.services.users.UsersServices

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain


/**
 * Clase que configura la seguridad de Spring y aplica filtros en los END_POINTS
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig
@Autowired constructor(
    private val userService: UsersServices,
    private val jwtTokenUtil: JwtTokenUtil
){
    /**
     * Configuración del Authentication Manager.
     * @param http http security.
     * @return el Authentication Manager ya creado y con el user detail service asignado.
     */
    @Bean
    fun authManager(http:HttpSecurity):AuthenticationManager{
        val authenticationManagerBuilder=http.getSharedObject(
            AuthenticationManagerBuilder::class.java
        )
        authenticationManagerBuilder.userDetailsService(userService)
        return authenticationManagerBuilder.build()
    }

    /**
     * Configuracion de la cadena de filtros.
     * @param http http security.
     * @return cadena de filtros de la seguridad ya configurada.
     */
    @Bean
    fun filterChain(http:HttpSecurity):SecurityFilterChain{
        val authenticationManager=authManager(http)

        http
            .csrf()
            .disable()
            .exceptionHandling()
            .and()
            .authenticationManager(authenticationManager)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .requestMatchers("/error/**").permitAll()

            //.requestMatchers("/api/**").permitAll() //esto permite todas las consultas a la api

            .requestMatchers("/api/users/login", "/api/users/register").permitAll()
            .requestMatchers("/api/users/list").permitAll()//.hasAnyRole("EMPLOYEE","ADMIN","SUPERADMIN")
            .requestMatchers("/api/users/me").permitAll()//.hasAnyRole("USER","EMPLOYEE","ADMIN","SUPERADMIN")
            .and()
            .addFilter(JwtAuthenticationFilter(jwtTokenUtil, authenticationManager))
            .addFilter(JwtAuthorizationFilter(jwtTokenUtil, userService, authenticationManager))

        return http.build()
    }
}