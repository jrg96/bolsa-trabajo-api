package com.empresa2.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.empresa2.api.entity.Usuario;
import com.empresa2.api.repository.UsuarioRepository;
import com.empresa2.api.service.interf.IUsuarioService;

@Service(value = "usuarioServiceJpa")
public class UsuarioServiceJpa implements UserDetailsService, IUsuarioService
{
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		Usuario usuario = usuarioRepository.findByUsername(username);
		if(usuario == null)
		{
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		
		return new User(usuario.getUsername(), usuario.getPassword(), getAuthority(usuario));
	}
	
	private Set<SimpleGrantedAuthority> getAuthority(Usuario usuario) 
	{
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        
		usuario.getPerfiles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getPerfil()));
		});
		
		return authorities;
	}

	@Override
	public void guardarUsuario(Usuario usuario) 
	{
		usuarioRepository.save(usuario);
	}

	@Override
	public Usuario obtenerUsuario(int id) 
	{
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		if (usuario.isPresent())
		{
			return usuario.get();
		}
		
		return null;
	}

	@Override
	public void eliminarUsuario(int id) 
	{
		if (this.usuarioRepository.existsById(id))
		{
			this.usuarioRepository.deleteById(id);
		}
	}

	@Override
	public Page<Usuario> buscarPagina(Pageable pageable) 
	{
		Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
		
		return usuarios;
	}

	@Override
	public boolean usuarioYaExiste(Usuario usuario) 
	{
		// Obtenemos un usuario de la DB dado email o username
		Usuario usuarioEmail = this.usuarioRepository.findByEmail(usuario.getEmail());
		Usuario usuarioUsern = this.usuarioRepository.findByUsername(usuario.getUsername());
		
		if (usuarioEmail != null || usuarioUsern != null)
		{
			return true;
		}
		return false;
	}
}
