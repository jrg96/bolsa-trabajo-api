package com.empresa2.api.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static com.empresa2.api.config.ConstantesJWT.HEADER_STRING;
import static com.empresa2.api.config.ConstantesJWT.TOKEN_PREFIX;


/*
 * Clase filtro ocupada para todos los request de la aplicacion a excepcion de
 * la URL /token/generate-token. El objetivo de este filtro es restringir el acceso
 * a los recursos mientras no se provea un JWT token valido.
 * */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenProvider jwtTokenUtil;
    
    private Collection<String> excludeUrlPatterns = new ArrayList<>();
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    
    /*public JwtAuthenticationFilter() 
    {
    	excludeUrlPatterns.add("/swagger-ui**");
    	excludeUrlPatterns.add("/swagger-ui/**");
    	excludeUrlPatterns.add("/api-docs/**");
	}*/

    // Metodo que implementa el filtro
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException 
    {
        String header = req.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;
        
        // Comprobamos si el request posee un header "Authorization" y comienza con la palabra
        // Bearer
        if (header != null && header.startsWith(TOKEN_PREFIX)) 
        {
            authToken = header.replace(TOKEN_PREFIX,"");
            try 
            {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } 
            catch (IllegalArgumentException e) 
            {
                logger.error("an error occured during getting username from token", e);
            } 
            catch (ExpiredJwtException e) 
            {
                logger.warn("the token is expired and not valid anymore", e);
            } 
            catch(SignatureException e){
                logger.error("Authentication Failed. Username or Password not valid.");
            }
        } else 
        {
            logger.warn("couldn't find bearer string, will ignore the header");
        }
        
        
        // Si existe un username, significa que el token existe, ahora terminamos de validar
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) 
        {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            /* verificamos explicitamente si el token sigue siendo valido
             * Caso de ser cierto, colocamos manualmente el usuario (ya no lo autenticamos con
             * contraseña porque si el token existe, consideramos valido)
             * 
             * Justo en este bloque, recuperamos los Authorities del usuario y los coolocamos en
             * el security context para ocuparlos despues
             * */
            if (jwtTokenUtil.validateToken(authToken, userDetails)) 
            {
                UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthentication(authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(req, res);
    }
    
    /*@Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException 
    {
    	System.out.println("Estamos intentando filtrar la URL: " + request.getServletPath());
    	boolean result = excludeUrlPatterns.stream()
                		.anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    	System.out.println("El resultado es: " + result);
        return result;
    }*/
}
