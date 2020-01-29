package com.empresa2.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * @EnableGlobalMethodSecurity(prePostEnabled = true) ocupada para habilitar la etiqueta @PreAuthorize
 * */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter 
{
	@Autowired
	private UserDetailsService userDetailsService;
	
	
	
	
	@Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
	
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/swagger-ui**", "/swagger-ui/**", "/api-docs/**");
    }
	
	
	/*
	 * Funcion ocupada para indicar a Spring Security, que nosotros queremos ocupar nuestra
	 * propia logica de autenticacion, encriptando las contraseñas con BCrypt en el proceso de
	 * autenticacion
	 * */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception 
	{
	    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	
	
	/*
	 * Funcion ocupada para indicar que todas las rutas a excepcion de /token/generate-oken
	 * deben encontrarse restringidas mientras no se provea un token JWT valido
	 * */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Deshabilitamos CORS y CSRF porque lo que queremos es que la API sea accesible
		// desde cualquier parte donde sea llamda
        http.cors().and().csrf().disable().
                authorizeRequests()
                .antMatchers("/token/*", "/swagger-ui**", "/swagger-ui/**", "/api-docs/**").permitAll()
                .anyRequest().authenticated();
                /*.and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/
        
        
        // Justo en esta linea agregamos el filtro de autenticacion JWT, asi este funcionara
        // para todo recurso que se quiera acceder a excepcion del /token/*
        http
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
	}
	
	
	
	
	/*
	 * Bean encargado de proveer el objeto JwtAuthenticationFilter, encargado de liberar
	 * un recurso solamente si el usuario provee un token valido
	 * */
	@Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception 
	{
        return new JwtAuthenticationFilter();
    }
	
	
	/*
	 * Bean ocupado para indicar a nuestro Custom authentication, que las contraseñas deben ser modificadas
	 * ocuapndo BCrypt (porque en la DB asi se encuentran encriptadas)
	 * */
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	
	/*
	 * Exponiendo el AuthenticationManager que necesitamos en el controlador AuthController
	 * */
	@Override
	@Bean
    public AuthenticationManager authenticationManagerBean() throws Exception 
	{
        return super.authenticationManagerBean();
	}
}
