package com.empresa2.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empresa2.api.config.TokenProvider;
import com.empresa2.api.model.AuthToken;
import com.empresa2.api.model.LoginUsuario;

/*
 * Controlador encargado de generar un token JWT valido en caso de realizar un login exitoso
 * 
 * */
@RestController
@RequestMapping("/token")
public class AuthController 
{
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private TokenProvider jwtTokenUtil;
	
	@PostMapping("/generate-token")
	public ResponseEntity<?> generarToken(@RequestBody LoginUsuario loginUsuario)
	{
		/*
		 * Antes de generar un token, primero debemos de autenticar al usuario, este proceso
		 * lo hacemos manualmente.
		 * 
		 * Mayor informacion en: https://www.baeldung.com/manually-set-user-authentication-spring-security
		 * */
		final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUsuario.getUsername(),
                        loginUsuario.getPassword()
                )
        );
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// Procedemos a intentar generar el token en base al resultado de la autenticacion
		final String token = jwtTokenUtil.generateToken(authentication);
		return ResponseEntity.ok(new AuthToken(token));
	}
}
