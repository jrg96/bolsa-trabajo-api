package com.empresa2.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empresa2.api.entity.Usuario;
import com.empresa2.api.model.UsuarioModel;
import com.empresa2.api.model.assembler.UsuarioModelAssembler;
import com.empresa2.api.model.exception.CustomNotFoundException;
import com.empresa2.api.service.interf.IUsuarioService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Locale;

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
	
	
	
	@PreAuthorize("hasAnyRole('ADMINISTRADOR')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtenerUsuario(@PathVariable("id") int id, 
			@RequestHeader(name = "Accept-Language", required = false) Locale locale)
	{
		/*
		 * ---------------- ZONA DE DESPLIEGUE DE DATOS ---------------------------------------
		 * */
		Usuario usuario = this.usuarioService.obtenerUsuario(id);
		
		if (usuario == null)
		{
			throw new CustomNotFoundException(
					messageSource.getMessage("not.found.usuario.message", null, locale) + id);
		}
		
		
		UsuarioModel usuarioModel = this.usuarioModelAssembler.toModel(usuario);
		usuarioModel.add(
				linkTo(methodOn(UsuarioController.class).obtenerListaUsuarios(null)).withRel("list-usuarios")
		);
		
		return ResponseEntity.ok(usuarioModel);
	}
	
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
