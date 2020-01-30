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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empresa2.api.entity.Usuario;
import com.empresa2.api.model.UsuarioModel;
import com.empresa2.api.model.assembler.UsuarioModelAssembler;
import com.empresa2.api.model.exception.CustomNotFoundException;
import com.empresa2.api.service.interf.IUsuarioService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
	
	
}
