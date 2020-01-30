package com.empresa2.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empresa2.api.entity.Usuario;
import com.empresa2.api.model.UsuarioModel;
import com.empresa2.api.model.assembler.UsuarioModelAssembler;
import com.empresa2.api.model.exception.CustomConstraintException;
import com.empresa2.api.model.exception.CustomGenericException;
import com.empresa2.api.model.exception.CustomNotFoundException;
import com.empresa2.api.service.interf.IUsuarioService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Date;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController 
{
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
    private PagedResourcesAssembler<Usuario> pagedResourcesAssembler;
	
	@Autowired
	private UsuarioModelAssembler usuarioModelAssembler;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	
	/*
	 * API endpoint para guardar un usuario
	 * */
	@PreAuthorize("hasAnyRole('ADMINISTRADOR')")
	@PostMapping(value = "/")
	public ResponseEntity<?> guardarUsuario(@Valid @RequestBody Usuario usuario)
	{
		/*
		 * ---------------- ZONA DE VALIDACION DE DATOS ---------------------------------------
		 * */
		// Chequeamos si un usuario con ese username o email ya existe en la DB
		if (this.usuarioService.usuarioYaExiste(usuario))
		{
			throw new CustomConstraintException(
					messageSource.getMessage("usuario.ya.existe.message", 
							null, LocaleContextHolder.getLocale()));
		}
		
		
		/*
		 * ---------------- ZONA DE DESPLIEGUE DE DATOS ---------------------------------------
		 * */
		// Encriptamos la password antes de guardar
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		this.usuarioService.guardarUsuario(usuario);
		
		
		
		// Devolvemos la propia entidad usuario, HATEOAS habilitado
		UsuarioModel usuarioModel = this.usuarioModelAssembler.toModel(usuario);
		usuarioModel.add(
				linkTo(methodOn(UsuarioController.class).obtenerListaUsuarios(null)).withRel("list-usuarios")
		);
		
		return ResponseEntity.ok(usuarioModel);
	}
	
	/*
	 * API Endpoint para obtener los datos de un usuario
	 * */
	@PreAuthorize("hasAnyRole('ADMINISTRADOR')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtenerUsuario(@PathVariable("id") int id)
	{
		/*
		 * ---------------- ZONA DE DESPLIEGUE DE DATOS ---------------------------------------
		 * */
		Usuario usuario = this.usuarioService.obtenerUsuario(id);
		
		if (usuario == null)
		{
			throw new CustomNotFoundException(
					messageSource.getMessage("not.found.usuario.message", 
							null, LocaleContextHolder.getLocale()) + id);
		}
		
		
		UsuarioModel usuarioModel = this.usuarioModelAssembler.toModel(usuario);
		usuarioModel.add(
				linkTo(methodOn(UsuarioController.class).obtenerListaUsuarios(null)).withRel("list-usuarios")
		);
		
		return ResponseEntity.ok(usuarioModel);
	}
	
	
	
	/*
	 * API Endpoint para obtener la lista de usuarios, paginada
	 * */
	@PreAuthorize("hasAnyRole('ADMINISTRADOR')")
	@GetMapping(value = "/list")
	public ResponseEntity<?> obtenerListaUsuarios(Pageable pageable)
	{
		/*
		 * ---------------- ZONA DE DESPLIEGUE DE DATOS ---------------------------------------
		 * */
		Page<Usuario> usuarios = this.usuarioService.buscarPagina(pageable);
		PagedModel<UsuarioModel> usuariosM = pagedResourcesAssembler.toModel(usuarios, usuarioModelAssembler);
		
		return ResponseEntity.ok(usuariosM);
	}
	
	
	/*
	 * API Endpoint para modificar un usuario (entidad completa)
	 * */
	@PreAuthorize("hasAnyRole('ADMINISTRADOR')")
	@PutMapping(value = "/edit/{id}")
	public ResponseEntity<?> modificarUsuario(@RequestBody Usuario usuario, @PathVariable("id") int id)
	{
		Usuario usuarioDB = this.usuarioService.obtenerUsuario(id);
		/*
		 * ---------------- ZONA DE VALIDACION DE DATOS ---------------------------------------
		 * */
		// Chequeamos si el usuario en cuestion siquiera se encuentra en la DB
		if (usuario == null)
		{
			throw new CustomNotFoundException(
					messageSource.getMessage("not.found.usuario.message", 
							null, LocaleContextHolder.getLocale()) + id);
		}
		
		// Chequeamos si username/email son distintos a los que se encuentran en la DB, y validamos
		if (!usuarioDB.getUsername().equals(usuario.getUsername()) || !usuarioDB.getEmail().equals(usuario.getEmail()))
		{
			if (this.usuarioService.usuarioYaExiste(usuario))
			{
				throw new CustomConstraintException(
						messageSource.getMessage("usuario.ya.existe.message", 
								null, LocaleContextHolder.getLocale()));
			}
		}
		
		
		
		/*
		 * ---------------- ZONA DE DESPLIEGUE DE DATOS ---------------------------------------
		 * */
		// Verficamos si la contraseña es distinta de la de 60 caracteres, para codificar otra vez
		if (usuario.getPassword().length() != 60)
		{
			usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		}
		
		// Colocamos el ID del usuario de la DB a la entidad usuario, y guardamos
		usuario.setId(usuarioDB.getId());
		this.usuarioService.guardarUsuario(usuario);
		
		// Devolvemos la propia entidad, habilitando HATEOAS
		UsuarioModel usuarioModel = this.usuarioModelAssembler.toModel(usuario);
		usuarioModel.add(
				linkTo(methodOn(UsuarioController.class).obtenerListaUsuarios(null)).withRel("list-usuarios")
		);
		
		return ResponseEntity.ok(usuarioModel);
	}
}
